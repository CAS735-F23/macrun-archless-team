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

		player, err := getPlayerDTO(c, query.Username)
		if err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		if err := app.StartGame(player, query.Location); err != nil {
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

		player, err := getPlayerDTO(c, query.Username)
		if err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		ctx, err := app.StopGame(player.Username /* use username from player dto */)
		if err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: "Game stopped, bye 👋",
			Data:    ctx,
		})
	}
}

type actionQuery struct {
	Username string `form:"username" binding:"required"`
	Action   string `form:"action" binding:"required"`
	Location string `form:"location"`
}

func handleGameAction(app *game.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		query := &actionQuery{}
		if err := c.ShouldBindJSON(query); err != nil {
			abortWithStatusMessage(c, http.StatusBadRequest, err)
			return
		}

		player, err := getPlayerDTO(c, query.Username)
		if err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		msg, err := app.ProcessGameAction(player, query.Location, query.Action)
		if err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: msg,
		})
	}
}
