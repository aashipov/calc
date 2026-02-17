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

func request(t *testing.T, url string, method string, statusCode int, reqBody io.Reader) (body string) {
	req, _ := http.NewRequest(method, url, reqBody)
	if reqBody != nil {
		req.Header.Add("Content-Type", TEXT_PLAIN)
	}
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		t.Errorf("Error making request: %v", err)
	}
	defer resp.Body.Close()
	if resp.StatusCode != statusCode {
		t.Errorf("API request failed with status code: %s", fmt.Errorf("%+v", resp))
	}
	bodyBytes, err := io.ReadAll(resp.Body)
	if err != nil {
		t.Errorf("Error reading response body: %s", err)
	}
	body = string(bodyBytes)
	return
}

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

func TestE2eWelcome(t *testing.T) {
	expected := string(Welcome)
	actual := request(t, fmt.Sprintf("http://%s/", baseUrl), "GET", 200, nil)
	if actual != expected {
		t.Errorf("Expected %s but got %v", expected, actual)
	}
}

func TestE2eExpression(t *testing.T) {
	actual := request(t, fmt.Sprintf("http://%s/", baseUrl), "POST", 200, strings.NewReader(expression))
	if actual != expressionResult {
		t.Errorf("Expected %s but got %v", expressionResult, actual)
	}
}

func TestE2eNan(t *testing.T) {
	actual := request(t, fmt.Sprintf("http://%s/", baseUrl), "POST", 200, strings.NewReader(NaN))
	if actual != NaN {
		t.Errorf("Expected %s but got %v", NaN, actual)
	}
}
