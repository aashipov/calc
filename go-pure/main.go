package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
)

var (
	httpPort = "8080"
)

func enableGracefulShutdown(server *http.Server) {
	gracefulShutdown := make(chan os.Signal, 1)
	signal.Notify(gracefulShutdown, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		sig := <-gracefulShutdown
		log.Printf("%s received, shutdown", sig)
		server.Close()
		os.Exit(0)
	}()
}

func main() {
	http.HandleFunc("/", CalcHandler)
	server := http.Server{Addr: ":" + httpPort, Handler: http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {
		CalcHandler(w, req)
	})}
	enableGracefulShutdown(&server)
	fmt.Println("Starting server on :" + httpPort)
	err := server.ListenAndServe()
	if err != nil {
		log.Fatal(err)
	}
}
