package main

import (
	"io"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"
)

var (
	expression       = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
	expressionResult = "19.988432890485228"
	NaN              = "NaN"
)

func checkResponse(t *testing.T, response *http.Response, expected string) {
	data, err := io.ReadAll(response.Body)
	if err != nil {
		t.Errorf("Error: %v", err)
	}
	if string(data) != expected {
		t.Errorf("Expected %s but got %v", expected, string(data))
	}
	if response.StatusCode != http.StatusOK {
		t.Errorf("Expected status OK but got %v", response.StatusCode)
	}
}

func TestWelcome(t *testing.T) {
	expected := string(welcome)
	r := httptest.NewRequest(http.MethodGet, "/", nil)
	w := httptest.NewRecorder()
	handler(w, r)
	response := w.Result()
	defer response.Body.Close()
	checkResponse(t, response, expected)
}

func TestExpression(t *testing.T) {
	r := httptest.NewRequest(http.MethodPost, "/", strings.NewReader(expression))
	w := httptest.NewRecorder()
	handler(w, r)
	response := w.Result()
	defer response.Body.Close()
	checkResponse(t, response, expressionResult)
}

func TestNaN(t *testing.T) {
	r := httptest.NewRequest(http.MethodPost, "/", strings.NewReader(NaN))
	w := httptest.NewRecorder()
	handler(w, r)
	response := w.Result()
	defer response.Body.Close()
	checkResponse(t, response, NaN)
}
