package game

import (
	"game-service/dto"
	"game-service/game/session"
)

func (app *App) StartGame(player *dto.PlayerDTO, location string) error {

	if err := app.StoreSession(player.Username, &session.Session{
		Player: player,
		Score:  0,
	}); err != nil {
		return err
	}

	return nil
}

func (app *App) StopGame(username string) error {
	s, err := app.GetSession(username)
	if err != nil {
		return err
	}

	_ = s // clean up session if any.

	if err = app.DeleteSession(username); err != nil {
		return err
	}

	return nil
}
