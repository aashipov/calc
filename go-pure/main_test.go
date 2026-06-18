package main

import (
	"fmt"
	"io"
	"net/http"
	"net/http/httptest"
	"os"
	"strings"
	"testing"
)

var baseUrl = NaN

func TestMain(m *testing.M) {
	http.HandleFunc("/", CalcHandler)
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {
		CalcHandler(w, req)
	}))
	baseUrl = server.Listener.Addr().String()
	defer server.Close()
	exitCode := m.Run()
	os.Exit(exitCode)
}

func request(t *testing.T, url string, method string, statusCode int, reqBody io.Reader) (body string) {
	req, _ := http.NewRequest(method, url, reqBody)
	if reqBody != nil {
		req.Header.Add("Content-Type", TextPlain)
	}
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		t.Errorf("Error making request: %v", err)
	}
	defer resp.Body.Close()
	if resp.StatusCode != statusCode {
		t.Errorf("API request failed with status code: %+v", resp)
	}
	bodyBytes, err := io.ReadAll(resp.Body)
	if err != nil {
		t.Errorf("Error reading response body: %s", err)
	}
	body = string(bodyBytes)
	return
}

func TestApp(t *testing.T) {
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
			var actual string
			if strings.EqualFold("POST", tt.requestMethod) {
				actual = request(t, fmt.Sprintf("http://%s/", baseUrl), tt.requestMethod, 200, strings.NewReader(tt.expression))
			} else {
				actual = request(t, fmt.Sprintf("http://%s/", baseUrl), tt.requestMethod, 200, nil)
			}
			if actual != tt.expected {
				t.Errorf("Expected %s but got %v", tt.expected, actual)
			}
		})
	}
}
