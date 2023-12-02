package context

import (
	"go.uber.org/atomic"

	"game-service/dto"
)

type Context struct {
	//Challenge struct{}
	Player    *dto.PlayerDTO
	heartRate atomic.Int64
	shelters  []dto.PointDTO
	location  dto.PointDTO
	Score     int
}

func (c *Context) UpdateHeartRate(v int) {
	c.heartRate.Store(int64(v))
}

func (c *Context) UpdateShelters(v []dto.PointDTO) {
	c.shelters = v
}

func (c *Context) GetLocation() dto.PointDTO {
	return c.location
}

func (c *Context) UpdateLocation(v dto.PointDTO) {
	c.location = v
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
	// add stop logic here if needed.
}
