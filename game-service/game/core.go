package game

import (
	"encoding/json"
	"fmt"
	"log"
	"math"
	"math/rand"
	"strings"

	"game-service/consts"
	"game-service/dto"
	"game-service/game/context"
	"game-service/message"
)

func (app *App) StartGame(player *dto.PlayerDTO, zone string, location dto.PointDTO) error {

	if _, ok := consts.AllowedZones[strings.ToUpper(zone)]; !ok {
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
			Data struct {
				Zone string
				Path []struct {
					Name        string
					Type        string
					CoordinateX float64
					CoordinateY float64
				}
			}
		}{}
		if err := app.GetService("geo-service", fmt.Sprintf("/geo/trail?zone=%s", strings.ToLower(zone)), &data); err != nil {
			return fmt.Errorf("get init trail failed: %v", err)
		}

		shelters := make([]dto.PointDTO, 0, len(data.Data.Path))
		for _, place := range data.Data.Path {
			if place.Type == "shelter" {
				shelters = append(shelters, dto.PointDTO{
					X: place.CoordinateX,
					Y: place.CoordinateY,
				})
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

		currentSpeed = math.Ceil(math.Sqrt(
			math.Pow(math.Abs(currLoc.X-prevLoc.X), 2) +
				math.Pow(math.Abs(currLoc.Y-prevLoc.Y), 2)))

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
			fmt.Sprintf("/challenge?userHeartRate=%d&type=%s", ctx.GetHeartRate(), cType), &data); err != nil {
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
			resp.Attack.Name = consts.AttackModes[rand.Intn(len(consts.AttackModes))]
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
			case consts.ReactSheltering:
				for _, shelter := range ctx.GetShelters() { // close to shelter...
					if math.Abs(shelter.X-location.X) < 2 && math.Abs(shelter.Y-location.Y) < 2 {
						resp.SetMessage(resp.GetMessage() + "(You're in the shelter, so you're safe now!)")
						ctx.UpdateAttackMode()
						resp.Attack.On = false
						resp.Attack.Name = ""
						ctx.ShelterCount++
						break
					}
					resp.SetMessage("(Find a shelter soon!)")
				}
			case consts.ReactEscaping:
				if currentSpeed > 20 { // run fast...
					resp.SetMessage(resp.GetMessage() + "(You're fast! Escaping succeeded!)")
					ctx.UpdateAttackMode()
					resp.Attack.On = false
					resp.Attack.Name = ""
					ctx.EscapeCount++
				} else {
					resp.SetMessage(resp.GetMessage() + fmt.Sprintf("(Run, %s run!)", player.Username))
				}
			case consts.ReactFighting:
				if ctx.GetHeartRate() > ((100-player.Age)/2+player.Weight)+rand.Intn(20)-10 { // fighting back...
					resp.SetMessage(resp.GetMessage() + "(You kicked it ass! Good job!)")
					ctx.UpdateAttackMode()
					resp.Attack.On = false
					resp.Attack.Name = ""
					ctx.FightCount++
				} else {
					resp.SetMessage(resp.GetMessage() + "(Fight it back! Go go go!)")
				}
			default:
				return nil, fmt.Errorf("unknown action mode: %s", action)
			}
		}
	}

	{ // Set basic attributes
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

	// Add Badges for player.
	for _, badge := range []struct {
		curCount, reqCount int
		name               string
	}{
		{ctx.EscapeCount, 2, consts.BadgeRunner},
		{ctx.FightCount, 2, consts.BadgeFighter},
		{ctx.ShelterCount, 2, consts.BadgeHider},
	} {
		if badge.curCount >= badge.reqCount {
			badgeReq := &dto.BadgeActionDTO{
				ActionDTO: dto.ActionDTO{
					Action:  consts.ActionAddBadge,
					Message: "add badge",
				},
			}

			badgeReq.BadgeAddRequest.BadgeName = badge.name
			badgeReq.BadgeAddRequest.Username = username
			badgeReq.BadgeAddRequest.Challenge = ctx.Working.Type

			data, err := json.Marshal(badgeReq)
			if err != nil {
				log.Printf("JSON encode badge request failed: %v", err)
			}
			log.Printf("New Badge JSON: %s", data)

			// Send Player Badge to Challenge with MQ Event.
			if err := message.SendMessageToQueue(app.cfg.RabbitMQ.URL, "", consts.GameToChallengeQueue, string(data)); err != nil {
				log.Printf("Send message to %s: %v", consts.GameToChallengeQueue, err)
			}
		}
	}

	// Make a shallow copy.
	co := &context.Context{
		Player:       ctx.Player,
		Score:        ctx.Score,
		FightCount:   ctx.FightCount,
		EscapeCount:  ctx.EscapeCount,
		ShelterCount: ctx.ShelterCount,
	}

	// Cleanup context.
	ctx.Close()

	if err = app.DeleteContext(username); err != nil {
		return nil, err
	}

	return co, nil
}
