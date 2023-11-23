package game

import (
	"fmt"

	"game-service/game/context"
)

func (app *App) StoreContext(name string, s *context.Context) error {
	_, ok := app.sessions[name]
	if ok {
		return fmt.Errorf("game context already exists: %s", name)
	}
	app.sessions[name] = s
	return nil
}

func (app *App) GetContext(name string) (*context.Context, error) {
	s, ok := app.sessions[name]
	if !ok {
		return nil, fmt.Errorf("game context not found: %s", name)
	}
	return s, nil
}

func (app *App) DeleteContext(name string) error {
	if s, err := app.GetContext(name); err != nil {
		return err
	} else {
		_ = s
		s.Close() // close session before delete.
	}
	delete(app.sessions, name)
	return nil
}
