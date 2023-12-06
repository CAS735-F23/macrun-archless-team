package api

import (
	"net/http"

	"github.com/gin-gonic/gin"

	"game-service/dto"
	"game-service/game"
)

type startQuery struct {
	Username string       `form:"username" binding:"required"`
	Zone     string       `form:"zone"`
	Location dto.PointDTO `form:"location"`
}

// handleGameStart godoc
// @Summary Start Game Session
// @Schemes
// @Description start game
// @Tags
// @Accept json
// @Produce json
// @Param   username  body      string  true  "player username"
// @Success 200 {object} responseMessage
// @Router /game/start [post]
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

		if err := app.StartGame(player, query.Zone, query.Location); err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		ctx, err := app.GetContext(player.Username)
		if err != nil {
			abortWithStatusMessage(c, http.StatusInternalServerError, err)
			return
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: "Game started successfully! 🥳",
			Data: gin.H{
				"shelters": ctx.GetShelters(),
			},
		})
	}
}

type stopQuery struct {
	Username string `form:"username" binding:"required"`
}

// handleGameStop godoc
// @Summary Stop Game Session
// @Schemes
// @Description stop game
// @Tags
// @Accept json
// @Produce json
// @Param   username  body      string  true  "player username"
// @Success 200 {object} responseMessage
// @Router /game/stop [post]
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

// handleGameAction godoc
// @Summary Update Game Action
// @Schemes
// @Description update game action
// @Tags
// @Accept json
// @Produce json
// @Param   username  body      string  true  "player username"
// @Param   action    body      string  false  "player action"
// @Param   type      body      string  true  "player workout type"
// @Param   location  body      dto.PointDTO  true  "player current location"
// @Success 200 {object} responseMessage
// @Router /game/action [post]
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
		if resp != nil && resp.GetMessage() != "" {
			msg = resp.GetMessage()
		}

		c.JSON(http.StatusOK, &responseMessage{
			Status:  http.StatusText(http.StatusOK),
			Message: msg,
			Data:    resp,
		})
	}
}
