package game

import (
	"encoding/json"
	"log"

	"game-service/config"
	"game-service/consts"
	"game-service/discovery"
	"game-service/dto"
	"game-service/game/session"
	"game-service/message"
)

type App struct {
	cfg *config.Config
	reg *discovery.Registry

	sessions map[string]*session.Session

	gameMQ *message.MQ
	hrmMQ  *message.MQ
}

func New(cfg *config.Config) (*App, error) {
	return &App{
		cfg:      cfg,
		sessions: make(map[string]*session.Session),
	}, nil
}

func (app *App) Start() {

	var err error

	// Service Discovery
	app.reg, err = discovery.New(app.cfg)
	if err != nil {
		log.Fatalln(err)
	}

	if err := app.reg.Register(); err != nil {
		log.Fatalln(err)
	}
	defer app.reg.Deregister()

	log.Printf("registered to discovery service: %s with %s:%d",
		app.cfg.Discovery.Service, app.cfg.Server.IP, app.cfg.Server.Port)

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
			msg := &dto.MessageDTO{}
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
			resp, _ := json.Marshal(&dto.MessageDTO{
				PlayerDTO: msg.PlayerDTO,
				GameType:  msg.GameType,
				Action:    statusAction,
				Message:   statusMsg,
			})
			log.Printf("send game status message to %s exchange: %s", consts.PlayerServiceExchange, resp)
			if err := message.SendMessage(
				app.cfg.RabbitMQ.URL,
				consts.PlayerServiceExchange,
				consts.GameStatusKey,
				string(resp)); err != nil {
				log.Printf("send game status message to %s exchange: %s", consts.PlayerServiceExchange, err)
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
