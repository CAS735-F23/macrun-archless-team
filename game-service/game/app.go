package game

import (
	"log"

	"game-service/config"
	"game-service/discovery"
)

type App struct {
	cfg *config.Config
	reg *discovery.Registry
}

func New(cfg *config.Config) (*App, error) {
	return &App{
		cfg: cfg,
	}, nil
}

func (app *App) Start() {

	// Service Discovery
	reg, err := discovery.New(app.cfg)
	if err != nil {
		log.Fatalln(err)
	}

	if err := reg.Register(); err != nil {
		log.Fatalln(err)
	}
	defer reg.Deregister()

	log.Println("game service started!")
}
