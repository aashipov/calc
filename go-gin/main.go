package main

import (
	"context"
	"fmt"
	"os"
	"os/signal"
	"syscall"

	"github.com/gin-gonic/gin"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	engine := gin.New()
	CalcHandler(engine)
	go func() {
		err := engine.Run()
		if err != nil {
			fmt.Errorf("Can not start HTTP server: %s", err)
			os.Exit(1)
		}
	}()

	<-ctx.Done()
	fmt.Println("Server exited")
}
