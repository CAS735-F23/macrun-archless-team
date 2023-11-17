package game

import (
	"fmt"
	"log"

	"game-service/consts"
	"game-service/dto"
)

func (app *App) handleGameStartEvent(msg *dto.Message) error {

	if msg.Action != consts.GameStartAction && msg.Action != consts.GameStopAction {
		return fmt.Errorf("unknown game action: %s", msg.Action)
	}

	// Game stop logic
	if msg.Action == consts.GameStopAction {
		if s, ok := app.sessions[msg.PlayerDTO.Username]; ok {
			s.Close()
			delete(app.sessions, msg.PlayerDTO.Username)
			log.Printf("User %s session deleted!", msg.PlayerDTO.Username)
		} else {
			return fmt.Errorf("session for %s not found", msg.PlayerDTO.Username)
		}
	}

	// Check session existence
	if _, ok := app.sessions[msg.PlayerDTO.Username]; ok {
		return fmt.Errorf("game %s for %s already started", msg.GameType, msg.PlayerDTO.Username)
	}

	fmt.Println(app.reg.Lookup("player-service"))

	session := &Session{
		Challenge: 0,
	}

	app.sessions[msg.PlayerDTO.Username] = session

	log.Printf("User %s session created!", msg.PlayerDTO.Username)

	return nil
}

type Session struct {
	Challenge int
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
