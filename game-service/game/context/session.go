package context

import (
	"go.uber.org/atomic"

	"game-service/dto"
)

type Context struct {
	//Challenge struct{}
	Player    *dto.PlayerDTO
	heartRate atomic.Int64
	Score     int
}

func (c *Context) UpdateHeartRate(v int) {
	c.heartRate.Store(int64(v))
}

func (c *Context) GetHeartRate() int {
	return int(c.heartRate.Load())
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
