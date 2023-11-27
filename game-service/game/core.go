package game

import (
	"time"

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

	initHeartRate := 0
	for i := 0; i < 5; i++ {
		initHeartRate = ctx.GetHeartRate()
		if initHeartRate >= 10 {
			break
		}
		time.Sleep(2 * time.Second)
	}

	return nil
}

func (app *App) StopGame(username string) error {
	s, err := app.GetContext(username)
	if err != nil {
		return err
	}

	_ = s // clean up session if any.

	if err = app.DeleteContext(username); err != nil {
		return err
	}

	return nil
}
