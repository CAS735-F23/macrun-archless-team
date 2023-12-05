package dto

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestActionResponseDTO(t *testing.T) {
	art := &ActionResponseDTO{
		message: "hello",
	}
	assert.Equal(t, "hello", art.GetMessage())

	art.SetMessage("world")
	assert.Equal(t, "world", art.GetMessage())

	art.ResetMessage()
	assert.Equal(t, "", art.GetMessage())
}
