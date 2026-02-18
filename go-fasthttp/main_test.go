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

func TestE2eWelcome(t *testing.T) {
	doTest(t, "http://localhost", "GET", "", string(Welcome))
}

func TestE2eExpression(t *testing.T) {
	doTest(t, "http://localhost", "POST", expression, expressionResult)
}

func TestE2eNan(t *testing.T) {
	doTest(t, "http://localhost", "POST", NaN, NaN)
}
