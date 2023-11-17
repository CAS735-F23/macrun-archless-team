package game

import (
	"encoding/json"
	"log"

	"game-service/config"
	"game-service/consts"
	"game-service/discovery"
	"game-service/dto"
	"game-service/message"
)

type App struct {
	cfg *config.Config
	reg *discovery.Registry

	sessions map[string]*Session

	gameMQ   *message.MQ
	hrmMQ    *message.MQ
	playerMQ *message.MQ
}

func New(cfg *config.Config) (*App, error) {
	return &App{
		cfg:      cfg,
		sessions: make(map[string]*Session),
	}, nil
}

func (app *App) Start() {

	var err error

	// Service Discovery
	app.reg, err = discovery.New(app.cfg)
	if err != nil {
		log.Fatalln(err)
	}

	log.Println("registering to discovery service!")

	if err := app.reg.Register(); err != nil {
		log.Fatalln(err)
	}
	defer app.reg.Deregister()

	// Player Message Queue
	app.playerMQ, err = message.New(
		app.cfg.RabbitMQ.URL,
		consts.PlayerServiceExchange,
		consts.GameStatusKey,
		consts.GameToPlayerQueue,
		true)
	if err != nil {
		log.Fatalln(err)
	}

	// Game Message Queue
	app.gameMQ, err = message.New(
		app.cfg.RabbitMQ.URL,
		consts.GameServiceExchange,
		consts.GameStartKey,
		consts.PlayerToGameQueue,
		true)
	if err != nil {
		log.Fatalln(err)
	}

	// HRM Message Queue
	app.hrmMQ, err = message.New(
		app.cfg.RabbitMQ.URL,
		consts.GameServiceExchange,
		consts.HRMStartKey,
		consts.HRMToGameQueue,
		true)
	if err != nil {
		log.Fatalln(err)
	}

	go func() {
		log.Printf("listen to %s queue.", consts.PlayerToGameQueue)

		for delivery := range app.gameMQ.MessageQueue() {
			msg := &dto.Message{}
			if err := json.Unmarshal(delivery.Body, msg); err != nil {
				log.Printf("decode json failed: %s", delivery.Body)
				continue
			}

			statusAction := consts.GameStatusActionOK
			statusMsg := "success!"
			if err := app.handleGameStartEvent(msg); err != nil {
				statusAction = consts.GameStatusActionFailed
				statusMsg = err.Error()
			}
			resp, _ := json.Marshal(&dto.Message{
				PlayerDTO: msg.PlayerDTO,
				GameType:  msg.GameType,
				Action:    statusAction,
				Message:   statusMsg,
			})
			log.Printf("send game status message to %s queue: %s", app.playerMQ.QueueName(), resp)
			if err := app.playerMQ.SendMessage(string(resp)); err != nil {
				log.Printf("send game status message to %s queue failed: %v", app.playerMQ.QueueName(), err)
			}
		}
	}()

	go func() {
		log.Printf("listen to %s queue.", consts.HRMToGameQueue)

		for delivery := range app.hrmMQ.MessageQueue() {
			_ = delivery
		}
	}()

	log.Println("game service started!")
}
