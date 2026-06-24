package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-fasthttp/swagger"
	"github.com/valyala/fasthttp"
)

const (
	httpPort = "8080"
)

// enableGracefulShutdown listens for SIGINT and SIGTERM signals and shuts down the server gracefully.
func enableGracefulShutdown(server *fasthttp.Server) {
	gracefulShutdown := make(chan os.Signal, 1)
	signal.Notify(gracefulShutdown, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		sig := <-gracefulShutdown
		log.Printf("%s received, shutdown", sig)
		server.ShutdownWithContext(context.Background())
	}()
}

func main() {
server := &fasthttp.Server{
	Handler:     CalcHandler,
	ReadTimeout:  5 * time.Second,
	WriteTimeout: 5 * time.Second,
}

	enableGracefulShutdown(server)
	err := server.ListenAndServe("0.0.0.0:" + httpPort)
	if err != nil {
		log.Fatalf("server failed: %v", err)
	}
}
