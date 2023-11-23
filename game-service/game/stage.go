package game

import (
	"fmt"

	"game-service/game/context"
)

func (app *App) StoreContext(name string, s *context.Context) error {
	app.mu.Lock()
	defer app.mu.Unlock()
	_, ok := app.sessions[name]
	if ok {
		return fmt.Errorf("game context already exists: %s", name)
	}
	app.sessions[name] = s
	return nil
}

func (app *App) GetContext(name string) (*context.Context, error) {
	app.mu.RLock()
	defer app.mu.RUnlock()
	s, ok := app.sessions[name]
	if !ok {
		return nil, fmt.Errorf("game context not found: %s", name)
	}
	return s, nil
}

func (app *App) DeleteContext(name string) error {
	app.mu.Lock()
	defer app.mu.Unlock()
	if s, ok := app.sessions[name]; ok {
		_ = s
		s.Close() // close session before delete.
		delete(app.sessions, name)
		return nil
	}
	return fmt.Errorf("game context not found: %s", name)
}
