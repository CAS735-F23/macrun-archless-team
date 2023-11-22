package game

import (
	"game-service/dto"
	"game-service/game/context"
)

func (app *App) StartGame(player *dto.PlayerDTO, location string) error {

	if err := app.StoreContext(player.Username, &context.Context{
		Player: player,
		Score:  0,
	}); err != nil {
		return err
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
