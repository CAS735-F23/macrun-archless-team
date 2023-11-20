package game

import (
	"fmt"
	"log"

	"game-service/consts"
	"game-service/dto"
)

func (app *App) handleGameStartEvent(msg *dto.MessageDTO) error {

	if msg.Action != consts.GameStartAction && msg.Action != consts.GameStopAction {
		return fmt.Errorf("unknown game action: %s", msg.Action)
	}

	// Game stop logic
	if msg.Action == consts.GameStopAction {
		if err := app.DeleteSessionByUser(msg.PlayerDTO.Username); err != nil {
			return err
		}
		log.Printf("User %s session has been deleted!", msg.PlayerDTO.Username)
	}

	// Check session existence
	if _, ok := app.sessions[msg.PlayerDTO.Username]; ok {
		return fmt.Errorf("game %s for %s already started", msg.GameType, msg.PlayerDTO.Username)
	}

	fmt.Println(app.reg.Lookup("player-service"))

	session := &Session{
		Challenge: struct{}{},
		Player:    msg.PlayerDTO,
		Score:     0, // init
	}

	if err := app.StoreSessionByUser(msg.PlayerDTO.Username, session); err != nil {
		return err
	}
	log.Printf("User %s session created!", msg.PlayerDTO.Username)

	return nil
}

func (app *App) StoreSessionByUser(name string, session *Session) error {
	_, ok := app.sessions[name]
	if ok {
		return fmt.Errorf("session already exists: %s", name)
	}
	app.sessions[name] = session
	return nil
}

func (app *App) GetSessionByUser(name string) (*Session, error) {
	s, ok := app.sessions[name]
	if !ok {
		return nil, fmt.Errorf("session not found: %s", name)
	}
	return s, nil
}

func (app *App) DeleteSessionByUser(name string) error {
	if ses, err := app.GetSessionByUser(name); err != nil {
		return err
	} else {
		ses.Close() // close session before delete.
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

type Session struct {
	Challenge struct{}
	Player    dto.PlayerDTO
	Score     int
}

func (s *Session) HandleReaction() {

}

func (s *Session) GenerateAttack() {

}

func (s *Session) CalcScore() {

}

func (s *Session) Close() {
	// TODO: add stop logic here if any.
}
