package game

import (
	"fmt"
	"log"
	"math"
	"math/rand"
	"strings"

	"game-service/dto"
	"game-service/game/context"
)

func (app *App) StartGame(player *dto.PlayerDTO, zone string, location dto.PointDTO) error {

	if _, ok := dto.AllowedZones[strings.ToUpper(zone)]; !ok {
		return fmt.Errorf("zone is not allowed: %s", zone)
	}

	ctx := &context.Context{
		Player: player,
		Score:  0,
	}

	if err := app.StoreContext(player.Username, ctx); err != nil {
		return err
	}

	{
		// A simple algorithm to calculate heart rate based on weight and age.
		initHeartRate := (100-player.Age)/2 + player.Weight
		ctx.UpdateHeartRate(initHeartRate)

		log.Printf("Game started with initial heart rate at %d", initHeartRate)
	}

	{ // Get Trails
		data := struct {
			Places []struct {
				Name       string
				Type       string
				Coordinate dto.PointDTO
			}
		}{}
		if err := app.GetService("geo-service", "/geo/trail?zone=mac", &data); err != nil {
			return fmt.Errorf("get init trail failed: %v", err)
		}

		shelters := make([]dto.PointDTO, 0, len(data.Places))
		for _, place := range data.Places {
			if place.Type == "shelter" {
				shelters = append(shelters, place.Coordinate)
			}
		}

		ctx.UpdateShelters(shelters)
		log.Printf("Shelters: %v", shelters)
	}

	{ // Location checks
		ctx.UpdateLocation(location)
	}

	return nil
}

func (app *App) ProcessGameAction(player *dto.PlayerDTO, action, cType string, location dto.PointDTO) (*dto.ActionResponseDTO, error) {

	ctx, err := app.GetContext(player.Username)
	if err != nil {
		return nil, err
	}
	ctx.Score++

	resp := &dto.ActionResponseDTO{}

	var currentSpeed float64
	{ // Location checks
		prevLoc := ctx.GetLocation()
		currLoc := location

		currentSpeed = math.Sqrt(
			math.Pow(math.Abs(currLoc.X-prevLoc.X), 2) +
				math.Pow(math.Abs(currLoc.Y-prevLoc.Y), 2))

		ctx.Score += int(currentSpeed)
		ctx.UpdateLocation(location)
	}

	if ctx.Working.Type != cType { // Get Challenge Type
		data := struct {
			Data struct {
				Description   string
				UserHeartRate int
				ExerciseCount int
			}
		}{}
		if err := app.GetService("challenge-service",
			fmt.Sprintf("/challenge/management?userHeartRate=%d&type=%s", ctx.GetHeartRate(), cType), &data); err != nil {
			//return nil, fmt.Errorf("get init challenge failed: %v", err)

			data.Data.UserHeartRate = ((100-player.Age)/2 + player.Weight) + rand.Intn(40) - 20
			data.Data.ExerciseCount = 10

			log.Printf("get challenge failed for %s with %v, use default value instead", cType, err)
		}

		ctx.Working.Type = cType
		ctx.Working.RequiredHeartRate = data.Data.UserHeartRate
		ctx.Working.ExerciseCount = data.Data.ExerciseCount
	}

	// Generate Attack
	if h := ctx.GetHeartRate(); h < ctx.Working.RequiredHeartRate-30 || h > ctx.Working.RequiredHeartRate+30 {
		if ctx.GetAttackMode() == "" {
			resp.Attack.On = true
			resp.Attack.Name = dto.AttackModes[rand.Intn(len(dto.AttackModes))]
			ctx.UpdateAttackMode(resp.Attack.Name)

			resp.SetMessage(fmt.Sprintf("You are under attack by %s, be careful! 😱", resp.Attack.Name))
		}
		ctx.Score -= 10 // Under Attack
	} else /* Clear Attack */ {
		if ctx.GetAttackMode() != "" {
			resp.Attack.On = false
			resp.Attack.Name = ""
			ctx.UpdateAttackMode() // Reset Attack
			ctx.Score += 1000      // Attack OK!

			resp.SetMessage("Congrats, attack is gone! 😊")
		}
		resp.ResetMessage()
	}

	if ctx.GetAttackMode() == "" && action != "" {
		resp.Attack.On = false
		resp.Attack.Name = ""
		resp.SetMessage(resp.GetMessage() + "(no reaction is needed for now)")
	} else if ctx.GetAttackMode() != "" {
		resp.Attack.On = true
		resp.Attack.Name = ctx.GetAttackMode()

		if action == "" {
			resp.SetMessage(resp.GetMessage() + "(your reaction is required now!)")
		} else {
			switch strings.ToUpper(action) {
			case dto.ReactSheltering:
				for _, shelter := range ctx.GetShelters() { // close to shelter...
					if math.Abs(shelter.X-location.X) < 2 && math.Abs(shelter.Y-location.Y) < 2 {
						resp.SetMessage(resp.GetMessage() + "(You're in the shelter, so you're safe now!)")
						ctx.UpdateAttackMode()
						resp.Attack.On = false
						resp.Attack.Name = ""
						break
					}
					resp.SetMessage("(Find a shelter soon!)")
				}
			case dto.ReactEscaping:
				if currentSpeed > 20 { // run fast...
					resp.SetMessage(resp.GetMessage() + "(You're fast! Escaping succeeded!)")
					ctx.UpdateAttackMode()
					resp.Attack.On = false
					resp.Attack.Name = ""
				} else {
					resp.SetMessage(resp.GetMessage() + fmt.Sprintf("(Run, %s run!)", player.Username))
				}
			case dto.ReactFighting:
				if ctx.GetHeartRate() > ((100-player.Age)/2+player.Weight)+rand.Intn(20)-10 { // fighting back...
					resp.SetMessage(resp.GetMessage() + "(You kicked it ass! Good job!)")
					ctx.UpdateAttackMode()
					resp.Attack.On = false
					resp.Attack.Name = ""
				} else {
					resp.SetMessage(resp.GetMessage() + "(Fight it back! Go go go!)")
				}
			default:
				return nil, fmt.Errorf("unknown action mode: %s", action)
			}
		}
	}

	{
		resp.Score = ctx.Score
		resp.Speed = currentSpeed
		resp.Location = ctx.GetLocation()
		resp.HeartRate = ctx.GetHeartRate()
	}

	return resp, nil
}

func (app *App) StopGame(username string) (*context.Context, error) {
	ctx, err := app.GetContext(username)
	if err != nil {
		return nil, err
	}

	// make a shallow copy.
	co := &context.Context{
		Player: ctx.Player,
		Score:  ctx.Score,
	}

	ctx.Close()

	if err = app.DeleteContext(username); err != nil {
		return nil, err
	}

	return co, nil
}
