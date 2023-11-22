package api

import (
	"fmt"
	"net"
	"net/http"
	"strconv"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/redis/go-redis/v9"

	"game-service/consts"
	"game-service/game"
)

func New(app *game.App) *gin.Engine {

	// Init Cache/Session Redis Client
	_cache = redis.NewClient(&redis.Options{
		Addr: net.JoinHostPort(
			app.Config().Redis.Host,
			strconv.Itoa(app.Config().Redis.Port)),
		Password: "", // no password set
		DB:       0,  // use default DB
	})

	// Init GIN Engine
	r := gin.New()
	{
		// register middleware
		r.Use(logger(), recovery())
		// fallback behavior
		r.NoRoute(notFound())
		r.NoMethod(notAllowed())
	}

	// index page
	r.GET("/", getIndex())

	gm := r.Group("/game")
	{
		gm.POST("/start", handleGameStart(app))
		gm.POST("/stop", handleGameStop(app))
		gm.POST("/action", handleGameAction(app))
	}

	return r
}

func getIndex() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.JSON(http.StatusOK, &responseMessage{
			Data: gin.H{
				"service": consts.GameServiceName,
			},
		})
	}
}

func logger() gin.HandlerFunc {
	return gin.LoggerWithConfig(gin.LoggerConfig{})
}

func recovery() gin.HandlerFunc {
	return gin.CustomRecovery(func(c *gin.Context, err any) {
		abortWithStatusMessage(c, http.StatusInternalServerError, err)
	})
}

func notFound() gin.HandlerFunc {
	return func(c *gin.Context) {
		abortWithStatusMessage(c, http.StatusNotFound, "")
	}
}

func notAllowed() gin.HandlerFunc {
	return func(c *gin.Context) {
		abortWithStatusMessage(c, http.StatusMethodNotAllowed, "")
	}
}

func abortWithStatusMessage(c *gin.Context, code int, message any) {
	c.AbortWithStatusJSON(code, &responseMessage{
		Status:  strings.ToUpper(http.StatusText(code)),
		Message: fmt.Sprintf("%v", message),
	})
}

type responseMessage struct {
	Status  string `json:"status"`
	Message string `json:"message,omitempty"`
	Data    any    `json:"data,omitempty"`
}
