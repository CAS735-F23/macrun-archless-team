package game

import (
	"fmt"
	"log"

	"game-service/config"
	"game-service/constant"
	"game-service/discovery"
	"game-service/message"
)

type App struct {
	cfg *config.Config
	reg *discovery.Registry

	playerMQ *message.MQ
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
	app.reg = reg

	log.Println("registering to discovery service!")

	if err := reg.Register(); err != nil {
		log.Fatalln(err)
	}
	defer reg.Deregister()

	// Message Queue
	playerMQ, err := message.New(
		app.cfg.RabbitMQ.URL,
		constant.PlayerServerExchange,
		constant.PlayerServerQueue,
	)
	if err != nil {
		log.Fatalln(err)
	}
	app.playerMQ = playerMQ

	log.Println("player service queue connected!")

	for msg := range playerMQ.MessageQueue() {
		fmt.Println(string(msg.Body))
	}

	log.Println("game service started!")
}
