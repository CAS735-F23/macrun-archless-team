package context

import (
	"go.uber.org/atomic"

	"game-service/dto"
)

type Context struct {
	heartRate  atomic.Int64
	shelters   []dto.PointDTO
	location   dto.PointDTO
	attackMode string

	FightCount   int
	EscapeCount  int
	ShelterCount int

	Player  *dto.PlayerDTO
	Working struct {
		Type              string
		RequiredHeartRate int
		ExerciseCount     int
	} `json:"-"`
	Score int
}

func (c *Context) GetAttackMode() string {
	return c.attackMode
}

func (c *Context) UpdateAttackMode(v ...string) {
	if len(v) == 0 {
		c.attackMode = ""
		return
	}
	for _, s := range v {
		c.attackMode = s
	}
}

func (c *Context) UpdateHeartRate(v int) {
	c.heartRate.Store(int64(v))
}

func (c *Context) GetShelters() []dto.PointDTO {
	return c.shelters
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

func (c *Context) Close() {}
