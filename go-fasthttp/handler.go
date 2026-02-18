package main

import (
	"strings"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-fasthttp/swagger"
	fastHttpSwagger "github.com/swaggo/fasthttp-swagger"
	"github.com/valyala/fasthttp"
)

const (
	SWAGGER    = "swagger"
	OPENAPI_UI = "openapi-ui"
)

var (
	Welcome = []byte("Welcome to calc service\nHTTP POST your expression")
)

// @Summary      Welcome
// @Description  Responds Welcome
// @Router       / [get]
// @Produce      text/plain
// @Success      200  {string} Welcome
func welcome(ctx *fasthttp.RequestCtx) {
	ctx.Write(Welcome)
}

// @Summary      Evaluate
// @Description  Responds Calculation Result
// @Router       / [post]
// @Param        body body string true "body"
// @Accept text/plain
// @Produce      text/plain
// @Success      200  {string} Evaluate
func evaluate(ctx *fasthttp.RequestCtx) {
	expression := string(ctx.Request.Body())
	result := CalculateViaExprtk(expression)
	ctx.Write([]byte(result))
}

func CalcHandler(ctx *fasthttp.RequestCtx) {
	path := string(ctx.RequestURI())
	method := string(ctx.Method())
	switch {
	case strings.Contains(path, OPENAPI_UI):
		fastHttpSwagger.WrapHandler(fastHttpSwagger.InstanceName(SWAGGER))(ctx)
	default:
		switch method {
		case "POST":
			evaluate(ctx)
		default:
			welcome(ctx)
		}
	}
}
