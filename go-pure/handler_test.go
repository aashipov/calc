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

func TestHandlerWelcome(t *testing.T) {
	expected := string(Welcome)
	r := httptest.NewRequest(http.MethodGet, "/", nil)
	w := httptest.NewRecorder()
	CalcHandler(w, r)
	response := w.Result()
	defer response.Body.Close()
	checkResponse(t, response, expected)
}

func TestHandlerExpression(t *testing.T) {
	r := httptest.NewRequest(http.MethodPost, "/", strings.NewReader(expression))
	w := httptest.NewRecorder()
	CalcHandler(w, r)
	response := w.Result()
	defer response.Body.Close()
	checkResponse(t, response, expressionResult)
}

func TestHandlerNaN(t *testing.T) {
	r := httptest.NewRequest(http.MethodPost, "/", strings.NewReader(NaN))
	w := httptest.NewRecorder()
	CalcHandler(w, r)
	response := w.Result()
	defer response.Body.Close()
	checkResponse(t, response, NaN)
}
