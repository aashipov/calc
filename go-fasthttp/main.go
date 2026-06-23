package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-fasthttp/swagger"
	"github.com/valyala/fasthttp"
)

const (
	httpPort = "8080"
)

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
		Handler: CalcHandler,
	}
	enableGracefulShutdown(server)
	err := server.ListenAndServe("0.0.0.0:" + httpPort)
	if err != nil {
		log.Fatal(err)
	}
}
