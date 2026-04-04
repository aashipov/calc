package main

import (
	"io"
	"net/http"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-echo/swagger"
	"github.com/labstack/echo/v5"
	echoSwagger "github.com/swaggo/echo-swagger/v2"
)

const (
	WELCOME    = "Welcome to calc service\nHTTP POST your expression"
)

// @Summary      Welcome
// @Description  Responds Welcome
// @Router       / [get]
// @Produce      text/plain
// @Success      200  {string} Welcome
func welcome(c *echo.Context) error {
	return c.String(http.StatusOK, WELCOME)
}

// @Summary      Evaluate
// @Description  Responds Calculation Result
// @Router       / [post]
// @Param        body body string true "body"
// @Accept text/plain
// @Produce      text/plain
// @Success      200  {string} Evaluate
func evaluate(c *echo.Context) error {
	bodyBytes, err := io.ReadAll(c.Request().Body)
	if err != nil {
		c.String(http.StatusOK, NaN)
	}
	expression := string(bodyBytes)
	resultString := CalculateViaExprtk(expression)
	return c.String(http.StatusOK, resultString)
}

func CalcHandler(server *echo.Echo) {
	server.GET("/openapi-ui/*", echoSwagger.WrapHandler)
	server.GET("/", welcome)
	server.POST("/", evaluate)
	server.POST("/mxparser", evaluate)
	server.POST("/exprtk", evaluate)
}
