package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"game-service/config"
	"game-service/discovery"
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

	// Service Discovery
	if err := discovery.Register(cfg); err != nil {
		log.Fatalln(err)
	}
	defer discovery.Deregister(cfg)

	// Main Entry
	time.Sleep(1000 * time.Hour)

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh
}
