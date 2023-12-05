package config

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestLoadConfigFromFile(t *testing.T) {
	_, err := LoadConfigFromFile("not-exists.yml")
	assert.Error(t, err)
}
