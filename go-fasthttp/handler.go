package main

import (
	"strings"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-fasthttp/swagger"
	fastHttpSwagger "github.com/swaggo/fasthttp-swagger"
	"github.com/valyala/fasthttp"
)

// @Summary      Welcome
// @Description  Responds Welcome
// @Router       / [get]
// @Produce      text/plain
// @Success      200  {string} Welcome
func welcome(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType(TextPlain)
	ctx.SetStatusCode(fasthttp.StatusOK)
	ctx.Write(Welcome)
}

// @Summary      ViaExprtk
// @Description  Responds Calculation Result
// @Router       / [post]
// @Router       /exprtk [post]
// @Param        body body string true "body"
// @Accept text/plain
// @Produce      text/plain
// @Success      200  {string} ViaExprtk
func viaExprtk(ctx *fasthttp.RequestCtx) {
	expression := string(ctx.Request.Body())
	result := CalculateViaExprtk(expression)
	ctx.SetContentType(TextPlain)
	ctx.SetStatusCode(fasthttp.StatusOK)
	ctx.WriteString(result)
}

// @Summary      ViaMeval
// @Description  Responds Calculation Result
// @Router       /meval [post]
// @Param        body body string true "body"
// @Accept text/plain
// @Produce      text/plain
// @Success      200  {string} ViaMeval
func viaMeval(ctx *fasthttp.RequestCtx) {
	expression := string(ctx.Request.Body())
	result := CalculateViaMeval(expression)
	ctx.SetContentType(TextPlain)
	ctx.SetStatusCode(fasthttp.StatusOK)
	ctx.WriteString(result)
}

func CalcHandler(ctx *fasthttp.RequestCtx) {
	uri := string(ctx.RequestURI())
	method := string(ctx.Method())
	switch {
	case strings.Contains(uri, OPENAPI_UI):
		fastHttpSwagger.WrapHandler(fastHttpSwagger.InstanceName(SWAGGER))(ctx)

	default:
		switch method {
		case fasthttp.MethodPost:
			path := string(ctx.Path())
			if strings.HasPrefix(path, "/meval") {
				viaMeval(ctx)
			} else {
				viaExprtk(ctx)
			}
		default:
			welcome(ctx)
		}
	}
}
