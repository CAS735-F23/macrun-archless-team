package game

import (
	"fmt"

	"game-service/dto"
	"game-service/game/session"
)

func (app *App) handleGameStartEvent(msg *dto.MessageDTO) error {

	//if msg.Action != consts.GameStartAction && msg.Action != consts.GameStopAction {
	//	return fmt.Errorf("unknown game action: %s", msg.Action)
	//}
	//
	//// Game stop logic
	//if msg.Action == consts.GameStopAction {
	//	if err := app.DeleteSession(msg.PlayerDTO.Username); err != nil {
	//		return err
	//	}
	//	log.Printf("User %s session has been deleted!", msg.PlayerDTO.Username)
	//}
	//
	//// Check session existence
	//if _, ok := app.sessions[msg.PlayerDTO.Username]; ok {
	//	return fmt.Errorf("game %s for %s already started", msg.GameType, msg.PlayerDTO.Username)
	//}
	//
	//fmt.Println(app.reg.Lookup("player-service"))
	//
	//session := &Session{
	//	Challenge: struct{}{},
	//	Player:    msg.PlayerDTO,
	//	Score:     0, // init
	//}
	//
	//if err := app.StoreSessionByUser(msg.PlayerDTO.Username, session); err != nil {
	//	return err
	//}
	//log.Printf("User %s session created!", msg.PlayerDTO.Username)

	return nil
}

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

type ReactOption uint8

const (
	Sheltering ReactOption = iota
	Escaping
	Fighting
)
