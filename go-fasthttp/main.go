package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/valyala/fasthttp"
)

// enableGracefulShutdown listens for SIGINT and SIGTERM signals and shuts down the server gracefully.
func enableGracefulShutdown(server *fasthttp.Server) {
	gracefulShutdown := make(chan os.Signal, 1)
	signal.Notify(gracefulShutdown, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		sig := <-gracefulShutdown
		log.Printf("%s received, shutdown", sig)
		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		server.ShutdownWithContext(ctx)
	}()
}

func main() {
	server := &fasthttp.Server{
		Handler:      CalcHandler,
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 5 * time.Second,
	}

	enableGracefulShutdown(server)
	err := server.ListenAndServe("0.0.0.0:" + HttpPort)
	if err != nil {
		log.Fatalf("server failed: %v", err)
	}
}
