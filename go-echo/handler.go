package main

import (
	"io"
	"net/http"

	"github.com/labstack/echo/v5"
)

const (
	SWAGGER    = "swagger"
	OPENAPI_UI = "openapi-ui"
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
