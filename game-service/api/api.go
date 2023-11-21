package api

import (
	"net/http"

	"github.com/gin-gonic/gin"

	"game-service/game"
)

type startQuery struct {
	Username string `form:"username" binding:"required"`
	Location string `form:"location"`
}

func handleGameStart(app *game.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		query := &startQuery{}
		if err := c.ShouldBindJSON(query); err != nil {
			abortWithStatusMessage(c, http.StatusBadRequest, err)
			return
		}

		if err := app.StartGame(query.Username, query.Location); err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: "Game started successfully!",
		})
	}
}

type stopQuery struct {
	Username string `form:"username" binding:"required"`
}

func handleGameStop(app *game.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		query := &stopQuery{}
		if err := c.ShouldBindJSON(query); err != nil {
			abortWithStatusMessage(c, http.StatusBadRequest, err)
			return
		}

		if err := app.StopGame(query.Username); err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: "Game stopped, bye 👋",
		})
	}
}

func handleGameAction(app *game.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Render(100, nil)
	}
}
