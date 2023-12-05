package context

import (
	"testing"

	"github.com/stretchr/testify/assert"

	"game-service/dto"
)

func TestContext(t *testing.T) {
	ctx := &Context{}

	{
		ctx.UpdateHeartRate(100)
		assert.Equal(t, 100, ctx.GetHeartRate())
	}

	{
		ctx.UpdateLocation(dto.PointDTO{X: 12, Y: 21})
		assert.Equal(t, float64(12), ctx.GetLocation().X)
		assert.Equal(t, float64(21), ctx.GetLocation().Y)
	}

	{
		shelters := []dto.PointDTO{{12, 21}, {13, 31}}
		ctx.UpdateShelters(shelters)
		for i, s := range ctx.GetShelters() {
			assert.Equal(t, shelters[i].X, s.X)
			assert.Equal(t, shelters[i].Y, s.Y)
		}
	}

	{
		ctx.UpdateAttackMode("TEST")
		assert.Equal(t, "TEST", ctx.GetAttackMode())
		ctx.UpdateAttackMode() // Reset
		assert.Equal(t, "", ctx.GetAttackMode())
	}

	{
		defer func() {
			r := recover()
			assert.Nil(t, r)
		}()

		ctx.Close()
	}
}
