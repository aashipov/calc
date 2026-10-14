package main

import (
	"context"
	"errors"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

func enableGracefulShutdown(server *http.Server) {
	gracefulShutdown := make(chan os.Signal, 1)
	signal.Notify(gracefulShutdown, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		sig := <-gracefulShutdown
		log.Printf("%s received, shutdown", sig)
		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		server.Shutdown(ctx)
	}()
}

func main() {
	server := http.Server{Addr: ":" + HttpPort, Handler: http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {
		CalcHandler(w, req)
	})}
	enableGracefulShutdown(&server)
	log.Printf("Starting server on : %s", HttpPort)
	err := server.ListenAndServe()
	if err != nil && !errors.Is(err, http.ErrServerClosed) {
		log.Fatal(err)
	}
}
