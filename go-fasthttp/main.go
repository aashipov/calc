package main

// #cgo CFLAGS: -O3 -I${SRCDIR}/go-exprtk-adapter
// #cgo CXXFLAGS: -O3
// #cgo LDFLAGS: -L${SRCDIR}/go-exprtk-adapter
// #include "go-exprtk-adapter.h"
// #include <stdlib.h>
import "C"
import (
	"log"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"strconv"
	goExprtkAdapter "bitbucket.org/anatoly_a_shipov/calc-go-fasthttp/go-exprtk-adapter"

	_ "bitbucket.org/anatoly_a_shipov/calc-go-fasthttp/swagger"
	fastHttpSwagger "github.com/swaggo/fasthttp-swagger"
	"github.com/valyala/fasthttp"
)

const (
	SWAGGER = "swagger"
	OPENAPI_UI = "openapi-ui"
	WELCOME = "Welcome to calc service\nHTTP POST your expression")

func enableGracefulShutdown(server *fasthttp.Server) {
	gracefulShutdown := make(chan os.Signal, 1)
	signal.Notify(gracefulShutdown, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		sig := <-gracefulShutdown
		log.Printf("%s received, shutdown", sig)
		server.Shutdown()
		os.Exit(0)
	}()
}

// @Summary      Welcome
// @Description  Responds Welcome
// @Router       / [get]
// @Produce      text/plain
// @Success      200  {string} Welcome
func welcome(ctx *fasthttp.RequestCtx) {
	ctx.Write([]byte(WELCOME))
}

// @Summary      Evaluate
// @Description  Responds Calculation Result
// @Router       / [post]
// @Param        body body string true "body"
// @Produce      text/plain
// @Success      200  {string} Evaluate
func evaluate(ctx *fasthttp.RequestCtx) {
	expression := string(ctx.Request.Body())
	result := goExprtkAdapter.Evaluate(expression)
	resultString := strconv.FormatFloat(result, 'f', -1, 64)
	ctx.Write([]byte(resultString))
}

func main() {
	handler := func(ctx *fasthttp.RequestCtx) {
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
	server := &fasthttp.Server{
		Handler: handler,
	}
	enableGracefulShutdown(server)
	server.ListenAndServe("0.0.0.0:8080")
}
