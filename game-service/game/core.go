package game

import (
	"fmt"

	"game-service/consts"
	"game-service/game/session"
)

func (app *App) StartGame(username, location string) error {

	isLoggedIn := false
	if err := app.GetService(
		consts.PlayerServiceName,
		fmt.Sprintf("/player/%s/is-logged-in", username),
		&isLoggedIn); err != nil {
		return err
	}

	if !isLoggedIn {
		return fmt.Errorf("user: %s is not logged in", username)
	}

	if err := app.StoreSession(username, &session.Session{
		Score: 0,
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
