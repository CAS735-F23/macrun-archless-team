package api

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/stretchr/testify/assert"

	"game-service/config"
	"game-service/consts"
	"game-service/game"
)

func TestNewServer(t *testing.T) {
	app, _ := game.New(&config.Config{})
	r := New(app)

	{
		w := httptest.NewRecorder()
		req, _ := http.NewRequest("GET", "/", nil)
		r.ServeHTTP(w, req)
		assert.Equal(t, http.StatusOK, w.Code)

		resp := responseMessage{}
		_ = json.Unmarshal(w.Body.Bytes(), &resp)
		assert.Equal(t, consts.GameServiceName, (resp.Data).(map[string]any)["service"])
	}

	{
		w := httptest.NewRecorder()
		req, _ := http.NewRequest("GET", "/not-found", nil)
		r.ServeHTTP(w, req)
		assert.Equal(t, http.StatusNotFound, w.Code)

		resp := responseMessage{}
		_ = json.Unmarshal(w.Body.Bytes(), &resp)
		assert.Equal(t, "NOT FOUND", resp.Status)
	}
}
