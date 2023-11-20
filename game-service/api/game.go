package api

import (
	"github.com/gin-gonic/gin"

	"game-service/game"
)

func handleAction(app *game.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Render(100, nil)
	}
}
