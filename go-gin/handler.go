package main

import (
	"net/http"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-gin/swagger"
	"github.com/gin-gonic/gin"
	swaggerfiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

const (
	WELCOME = "Welcome to calc service\nHTTP POST your expression"
)

// @Summary      Welcome
// @Description  Responds Welcome
// @Router       / [get]
// @Produce      text/plain
// @Success      200  {string} Welcome
func welcome(ctx *gin.Context) {
	ctx.String(http.StatusOK, WELCOME)
}

// @Summary      Evaluate
// @Description  Responds Calculation Result
// @Router       / [post]
// @Param        body body string true "body"
// @Accept text/plain
// @Produce      text/plain
// @Success      200  {string} Evaluate
func evaluate(ctx *gin.Context) {
	bodyBytes, err := ctx.GetRawData()
	if err != nil {
		ctx.String(http.StatusOK, NaN)
		return
	}
	expression := string(bodyBytes)
	resultString := CalculateViaExprtk(expression)
	ctx.String(http.StatusOK, resultString)
}

func CalcHandler(engine *gin.Engine) {
	{
		engine.GET("/openapi-ui/*any", ginSwagger.WrapHandler(swaggerfiles.Handler))
		engine.GET("/", welcome)
		engine.POST("/", evaluate)
		engine.POST("/mxparser", evaluate)
		engine.POST("/exprtk", evaluate)
	}
}
