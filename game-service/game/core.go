package game

import (
	"fmt"
	"log"
	"math"
	"math/rand"

	"game-service/dto"
	"game-service/game/context"
)

func (app *App) StartGame(player *dto.PlayerDTO, location dto.PointDTO) error {

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
		resp.Attack.On = true
		resp.Attack.Name = dto.AttackModes[rand.Intn(len(dto.AttackModes))]
	} else /* Clear Attack */ {
		resp.Attack.On = false
		resp.Attack.Name = ""
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
