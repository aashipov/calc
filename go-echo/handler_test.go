package main

import (
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/labstack/echo/v5"
)

func checkResult(t *testing.T, rec *httptest.ResponseRecorder, expected string, err error) {
	if err != nil {
		t.Fatalf("error = %v", err)
	}
	if rec.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, rec.Code)
	}
	if rec.Body.String() != expected {
		t.Errorf("Expected body %q, got %q", expected, rec.Body.String())
	}
}

func TestWelcome(t *testing.T) {
	e := echo.New()
	req := httptest.NewRequest(http.MethodGet, "/", nil)
	rec := httptest.NewRecorder()
	c := e.NewContext(req, rec)

	err := welcome(c)
	checkResult(t, rec, WELCOME, err)
}

func TestEvaluate(t *testing.T) {
	e := echo.New()

	tests := []struct {
		name       string
		expression string
		expected   string
	}{
		{"simple addition", "2+2", "4"},
		{"complex expression", "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2", "19.988432890485228"},
		{"invalid expression", "invalid", NaN},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			req := httptest.NewRequest(http.MethodPost, "/", strings.NewReader(tt.expression))
			req.Header.Set(echo.HeaderContentType, echo.MIMETextPlain)
			rec := httptest.NewRecorder()
			c := e.NewContext(req, rec)

			err := evaluate(c)
			checkResult(t, rec, tt.expected, err)
		})
	}
}
