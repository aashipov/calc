package main

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/labstack/echo/v5"
)

func checkResult(t *testing.T, rec *httptest.ResponseRecorder, expected string) {
	if rec.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, rec.Code)
	}
	if rec.Body.String() != expected {
		t.Errorf("Expected body %q, got %q", expected, rec.Body.String())
	}
}

func TestCalcHandler(t *testing.T) {
	tests := []struct {
		name          string
		requestMethod string
		expression    string
		expected      string
	}{
		{"welcome", "GET", "", WELCOME},
		{"simple addition", "POST", "2+2", "4"},
		{"complex expression", "POST", "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2", "19.988432890485228"},
		{"invalid expression", "POST", "invalid", NaN},
	}

	server := echo.New()
	CalcHandler(server)
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			rec := httptest.NewRecorder()
			req := httptest.NewRequest(tt.requestMethod, "/", strings.NewReader(tt.expression))
			req.Header.Set(echo.HeaderContentType, echo.MIMETextPlain)
			server.ServeHTTP(rec, req)
			checkResult(t, rec, tt.expected)
		})
	}
}
