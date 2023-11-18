package main

import (
	"log"
	"net"
	"net/http"
	"strconv"

	"game-service/api"
	"game-service/config"
	"game-service/game"
	"game-service/utils"
)

func init() {
	// nop
}

func main() {

	// Load Config
	cfg, err := config.LoadConfigFromFile("./resources/application.yml")
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

	var (
		addr   = net.JoinHostPort("", strconv.Itoa(cfg.Server.Port))
		router = api.New(app)
	)
	log.Printf("API server is running at %s", addr)
	if err = http.ListenAndServe(addr, router); err != nil {
		log.Fatal(err)
	}
}
