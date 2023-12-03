package api

import (
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"

	"game-service/dto"
	"game-service/game"
)

type startQuery struct {
	Username string       `form:"username" binding:"required"`
	Location dto.PointDTO `form:"location"`
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
			Message: "Game started successfully! 🥳",
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
			Message: "Game stopped, bye! 👋",
			Data:    ctx,
		})
	}
}

type actionQuery struct {
	Username string       `form:"username" binding:"required"`
	Action   string       `form:"action"`
	Type     string       `form:"type"`
	Location dto.PointDTO `form:"location"`
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

		var resp *dto.ActionResponseDTO
		if resp, err = app.ProcessGameAction(player, query.Action, query.Type, query.Location); err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		msg := "Game action handled, keep on! 💪"
		if resp != nil && resp.Attack.On {
			msg = fmt.Sprintf("You are under attack by %s, be careful! 😱", resp.Attack.Name)
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: msg,
			Data:    resp,
		})
	}
}
