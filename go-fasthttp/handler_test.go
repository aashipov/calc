package main

import (
	"strings"
	"testing"

	"github.com/valyala/fasthttp"
)

func checkResponse(t *testing.T, ctx *fasthttp.RequestCtx, expected string) {
	t.Helper()
	if ctx.Response.StatusCode() != fasthttp.StatusOK {
		t.Errorf("Expected status %d, got %d", fasthttp.StatusOK, ctx.Response.StatusCode())
	}
	if string(ctx.Response.Body()) != expected {
		t.Errorf("Expected body %q, got %q", expected, ctx.Response.Body())
	}
}

func TestCalcHandler(t *testing.T) {
	tests := []struct {
		name          string
		requestMethod string
		expression    string
		expected      string
	}{
		{"welcome", "GET", "", string(Welcome)},
		{"simple addition", "POST", "2+2", "4"},
		{"complex expression", "POST", "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2", "19.988432890485228"},
		{"invalid expression", "POST", "invalid", NaN},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ctx := &fasthttp.RequestCtx{}
			ctx.Request.SetRequestURI("/")
			ctx.Request.Header.SetMethod(tt.requestMethod)
			if strings.EqualFold("POST", tt.requestMethod) {
				ctx.Request.SetBody([]byte(tt.expression))
			}
			CalcHandler(ctx)
			checkResponse(t, ctx, string(tt.expected))
		})
	}
}
