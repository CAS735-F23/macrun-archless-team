package game

import (
	"encoding/json"
	"log"
	"sync"

	"game-service/config"
	"game-service/consts"
	"game-service/discovery"
	"game-service/dto"
	"game-service/game/context"
	"game-service/message"
)

type App struct {
	cfg *config.Config
	reg *discovery.Registry

	hrmMQ *message.MQ

	mu       sync.RWMutex
	sessions map[string]*context.Context
}

func New(cfg *config.Config) (*App, error) {
	return &App{
		cfg:      cfg,
		sessions: make(map[string]*context.Context),
	}, nil
}

func (app *App) Config() *config.Config {
	return app.cfg
}

func (app *App) Start() {

	var err error

	// Service Discovery
	app.reg, err = discovery.New(app.cfg)
	if err != nil {
		log.Fatalln(err)
	}

	// WHY: game service does not need to be discovered.
	//
	//if err := app.reg.Register(); err != nil {
	//	log.Fatalln(err)
	//}
	//defer app.reg.Deregister()

	log.Printf("registered to discovery service: %s with %s:%d",
		app.cfg.Discovery.Service, app.cfg.Server.IP, app.cfg.Server.Port)

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
		log.Printf("listen to %s queue.", consts.HRMToGameQueue)

		for delivery := range app.hrmMQ.MessageQueue() {
			msg := struct {
				dto.ActionDTO
				HeartRateDto dto.HeartRateDTO
			}{}
			if err := json.Unmarshal(delivery.Body, &msg); err != nil {
				log.Printf("hrm message: unmarshal failed: %v", err)
				continue
			}

			if msg.Action != consts.HRMSendAction {
				log.Printf("hrm message: invalid action: %s", msg.Action)
				continue
			}

			if s, err := app.GetContext(msg.HeartRateDto.Username); err != nil {
				log.Printf("hrm message: get session failed for user: %s", msg.HeartRateDto.Username)
			} else {
				s.UpdateHeartRate(msg.HeartRateDto.HeartRate)
				log.Printf("hrm message: set heart rate for user: %s (%d)",
					msg.HeartRateDto.Username, msg.HeartRateDto.HeartRate)
			}
		}
	}()

	log.Println("game service started!")
}
