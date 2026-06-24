package main

import (
	"context"
	"log"
	"net/http"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	engine := gin.Default()
	CalcHandler(engine)
	srv := &http.Server{Addr: ":8080", Handler: engine}
	go func() {
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Printf("ListenAndServe(): %v", err)
		}
	}()

	<-ctx.Done()
	log.Println("Shutting down server...")
	ctxShutdown, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctxShutdown); err != nil {
		log.Printf("Server shutdown error: %v", err)
	}
	log.Println("Server exited")
}
