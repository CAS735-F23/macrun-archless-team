package game

import (
	"log"

	"game-service/dto"
	"game-service/game/context"
)

func (app *App) StartGame(player *dto.PlayerDTO, location string) error {

	ctx := &context.Context{
		Player: player,
		Score:  0,
	}

	if err := app.StoreContext(player.Username, ctx); err != nil {
		return err
	}

	// A simple algorithm to calculate heart rate based on weight and age.
	initHeartRate := (100-player.Age)/2 + player.Weight

	log.Printf("Game started with initial heart rate at %d", initHeartRate)

	return nil
}

func (app *App) ProcessGameAction(player *dto.PlayerDTO, location, action string) (string, error) {
	return "", nil
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
