package game

import (
	"fmt"

	"game-service/game/session"
)

func (app *App) StoreSession(name string, s *session.Session) error {
	_, ok := app.sessions[name]
	if ok {
		return fmt.Errorf("session already exists: %s", name)
	}
	app.sessions[name] = s
	return nil
}

func (app *App) GetSession(name string) (*session.Session, error) {
	s, ok := app.sessions[name]
	if !ok {
		return nil, fmt.Errorf("session not found: %s", name)
	}
	return s, nil
}

func (app *App) DeleteSession(name string) error {
	if s, err := app.GetSession(name); err != nil {
		return err
	} else {
		_ = s
		s.Close() // close session before delete.
	}
	delete(app.sessions, name)
	return nil
}
