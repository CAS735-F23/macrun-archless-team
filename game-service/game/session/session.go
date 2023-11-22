package session

import (
	"game-service/dto"
)

type Session struct {
	//Challenge struct{}
	Player dto.PlayerDTO
	Score  int
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
