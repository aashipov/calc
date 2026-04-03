package main

import (
	"log/slog"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-echo/swagger"
	"github.com/labstack/echo/v5"
	echoSwagger "github.com/swaggo/echo-swagger/v2"
)

func router(server *echo.Echo) {
	server.GET("/openapi-ui/*", echoSwagger.WrapHandler)
	server.GET("/", welcome)
	server.POST("/", evaluate)
	server.POST("/mxparser", evaluate)
	server.POST("/exprtk", evaluate)
}

func main() {
	server := echo.New()
	router(server)
	if err := server.Start("0.0.0.0:8080"); err != nil {
		slog.Error("failed to start server", "error", err)
	}
}
