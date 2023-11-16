package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"

	"game-service/config"
	"game-service/game"
	"game-service/utils"
)

func init() {
	// nop
}

func main() {

	// Load Config
	cfg, err := config.LoadConfigFromFile("application.yml")
	if err != nil {
		log.Fatalln(err)
	}

	if cfg.Server.IP == "" {
		cfg.Server.IP = utils.GetPrimaryIP(cfg.Discovery.Host, cfg.Discovery.Port)
	}

	// Run Service
	app, err := game.New(cfg)
	if err != nil {
		log.Fatalln(err)
	}
	go app.Start()

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh
}
