package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"

	"game-service/config"
	"game-service/discovery"
	"game-service/rabbitmq"
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
	go rabbitmq.Send(cfg.RabbitMQ.URL, "testing")
	rabbitmq.Receive(cfg.RabbitMQ.URL, "testing")

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh
}
