package main

import (
	"testing"

	"github.com/valyala/fasthttp"
)

var (
	expression       = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
	expressionResult = "19.988432890485228"
	NaN              = "NaN"
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
	handler(ctx)
	checkResponse(t, ctx, string(WELCOME))
}

func TestExpression(t *testing.T) {
	ctx := &fasthttp.RequestCtx{}
	ctx.Request.SetRequestURI("/")
	ctx.Request.Header.SetMethod("POST")
	ctx.Request.SetBody([]byte(expression))
	handler(ctx)
	checkResponse(t, ctx, string(expressionResult))
}

func TestNaN(t *testing.T) {
	ctx := &fasthttp.RequestCtx{}
	ctx.Request.SetRequestURI("/")
	ctx.Request.Header.SetMethod("POST")
	ctx.Request.SetBody([]byte(NaN))
	handler(ctx)
	checkResponse(t, ctx, string(NaN))
}
