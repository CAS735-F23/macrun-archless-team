package api

import (
	"context"
	"encoding/base64"
	"encoding/json"
	"errors"
	"fmt"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/jkeys089/jserial"
	"github.com/redis/go-redis/v9"

	"game-service/dto"
)

var _cache *redis.Client

func getPlayerDTOBySessionID(sessionID, username string) (*dto.PlayerDTO, error) {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	cmd := _cache.HGet(ctx,
		fmt.Sprintf("spring:session:sessions:%s", sessionID),
		fmt.Sprintf("sessionAttr:PLAYER_SESSION%s", username))

	data, err := cmd.Bytes()
	if err != nil {
		return nil, err
	}

	res, err := jserial.ParseSerializedObjectMinimal(data)
	if err != nil {
		return nil, err
	}

	if len(res) == 0 {
		return nil, errors.New("ParseSerializedObjectMinimal: empty results")
	}

	obj, ok := res[0].(string)
	if !ok {
		return nil, fmt.Errorf("invalid result type: %T", res[0])
	}

	player := &dto.PlayerDTO{}
	err = json.Unmarshal([]byte(obj), player)
	return player, err
}

func getPlayerDTO(c *gin.Context, username string) (*dto.PlayerDTO, error) {
	id, err := getSessionID(c)
	if err != nil {
		return nil, err
	}
	return getPlayerDTOBySessionID(id, username)
}

func getSessionID(c *gin.Context) (string, error) {
	cookie, err := c.Cookie("SESSION")
	if err != nil {
		return "", err
	}

	id, err := base64.StdEncoding.DecodeString(cookie)
	return string(id), err
}
