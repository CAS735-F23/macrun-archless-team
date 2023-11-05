package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"game-service/discovery"
)

func init() {

}

func main() {

	// Service Discovery
	err := discovery.InitClient("127.0.0.1", 8848)
	if err != nil {
		log.Fatalln(err)
	}
	err = discovery.Register("192.168.2.11", 8849)
	if err != nil {
		log.Fatalln(err)
	}
	//defer discovery.Deregister("10.0.0.1", 9090)

	time.Sleep(1000 * time.Second)

	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh
}
