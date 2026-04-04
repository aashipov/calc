package main

import (
	"log/slog"

	"github.com/labstack/echo/v5"
)

func main() {
	server := echo.New()
	CalcHandler(server)
	if err := server.Start("0.0.0.0:8080"); err != nil {
		slog.Error("failed to start server", "error", err)
	}
}
