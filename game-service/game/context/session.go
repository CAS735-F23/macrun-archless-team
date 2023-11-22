package context

import (
	"game-service/dto"
)

type Context struct {
	//Challenge struct{}
	Player    *dto.PlayerDTO
	HeartRate int
	Score     int
}

func (c *Context) UpdateHeartRate(v int) {
	c.HeartRate = v
}

func (c *Context) HandleReaction() {

}

func (c *Context) GenerateAttack() {

}

func (c *Context) CalcScore() {

}

func (c *Context) Close() {
	// TODO: add stop logic here if any.
}
