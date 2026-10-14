package main

import (
	"io"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
)

func checkResponse(t *testing.T, response *http.Response, expected string) {
	actualBytes, err := io.ReadAll(response.Body)
	if err != nil {
		t.Errorf("Error: %v", err)
	}
	actual := string(actualBytes)
	if response.StatusCode != http.StatusOK {
		t.Errorf("Expected status OK but got %v", response.StatusCode)
	}
	if actual != expected {
		t.Errorf("Expected %s but got %v", expected, actual)
	}
}

func TestCalcHandler(t *testing.T) {
	tests := []struct {
		name          string
		requestMethod string
		path          string
		expression    string
		expected      string
	}{
		{"welcome", "GET", "/", "", string(Welcome)},
		{"exprtk: simple addition", "POST", "/", "2+2", "4"},
		{"exprtk: complex expression", "POST", "/", "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2", "19.988432890485228"},
		{"exprtk: invalid expression", "POST", "/", "invalid", NaN},
		{"meval: simple addition", "POST", "/meval", "2+2", "4"},
		{"meval: complex expression", "POST", "/meval", "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2", "19.988432890485228"},
		{"meval: invalid expression", "POST", "/meval", "invalid", NaN},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			var r *http.Request
			if strings.EqualFold("POST", tt.requestMethod) {
				r = httptest.NewRequest(http.MethodPost, tt.path, strings.NewReader(tt.expression))
			} else {
				r = httptest.NewRequest(tt.requestMethod, tt.path, nil)
			}
			w := httptest.NewRecorder()
			CalcHandler(w, r)
			response := w.Result()
			defer response.Body.Close()
			checkResponse(t, response, tt.expected)
		})
	}
}
