package main

import (
	"log"
	"net"
	"os"
	"strings"
	"testing"

	"github.com/valyala/fasthttp"
	"github.com/valyala/fasthttp/fasthttputil"
)

var listener *fasthttputil.InmemoryListener

func TestMain(m *testing.M) {
	listener = fasthttputil.NewInmemoryListener()
	go func() {
		err := fasthttp.Serve(listener, CalcHandler)
		if err != nil {
			log.Fatalf("Test fasthttp server failed: %v", err.Error())
		}
	}()
	defer listener.Close()
	exitCode := m.Run()
	os.Exit(exitCode)
}

func doTest(t *testing.T, url string, method string, requestBody string, expected string) {
	client := &fasthttp.Client{
		Dial: func(addr string) (net.Conn, error) {
			return listener.Dial()
		},
	}
	req := fasthttp.AcquireRequest()
	defer fasthttp.ReleaseRequest(req)
	req.Header.SetMethod(method)
	req.SetRequestURI(url)
	if strings.EqualFold("POST", method) {
		req.SetBody([]byte(requestBody))
	}
	resp := fasthttp.AcquireResponse()
	defer fasthttp.ReleaseResponse(resp)
	if err := client.Do(req, resp); err != nil {
		t.Errorf("Client failed: %v", err)
	}
	if resp.StatusCode() != fasthttp.StatusOK {
		t.Errorf("Unexpected status code: %d", resp.StatusCode())
	}
	actual := resp.Body()
	if expected != string(actual) {
		t.Errorf("Unexpected body: %q", actual)
	}
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
			doTest(t, "http://localhost/", tt.requestMethod, tt.expression, tt.expected)
		})
	}
}
