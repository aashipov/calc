package main

import (
	"testing"

	"github.com/valyala/fasthttp"
)

func checkResponse(t *testing.T, ctx *fasthttp.RequestCtx, expected string) {
	if ctx.Response.StatusCode() != fasthttp.StatusOK {
		t.Errorf("Expected status %d, got %d", fasthttp.StatusOK, ctx.Response.StatusCode())
	}
	if string(ctx.Response.Body()) != expected {
		t.Errorf("Expected body %q, got %q", expected, ctx.Response.Body())
	}
}

func TestWelcome(t *testing.T) {
	ctx := &fasthttp.RequestCtx{}
	ctx.Request.SetRequestURI("/")
	ctx.Request.Header.SetMethod("GET")
	CalcHandler(ctx)
	checkResponse(t, ctx, string(Welcome))
}

func TestExpression(t *testing.T) {
	ctx := &fasthttp.RequestCtx{}
	ctx.Request.SetRequestURI("/")
	ctx.Request.Header.SetMethod("POST")
	ctx.Request.SetBody([]byte(expression))
	CalcHandler(ctx)
	checkResponse(t, ctx, string(expressionResult))
}

func TestNaN(t *testing.T) {
	ctx := &fasthttp.RequestCtx{}
	ctx.Request.SetRequestURI("/")
	ctx.Request.Header.SetMethod("POST")
	ctx.Request.SetBody([]byte(NaN))
	CalcHandler(ctx)
	checkResponse(t, ctx, string(NaN))
}
