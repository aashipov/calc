package main

import (
	"io"
	"net/http"
)

const (TEXT_PLAIN = "text/plain")

var (
	Welcome = []byte("Welcome to calc service\nHTTP POST your expression")
)

func CalcHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", TEXT_PLAIN)
	if "GET" == r.Method {
		w.Write(Welcome)
		return
	} else {
		bodyBytes, err := io.ReadAll(r.Body)
		if err != nil {
			w.Write([]byte(err.Error()))
			return
		}
		expression := string(bodyBytes)
		resultString := CalculateViaExprtk(expression)
		w.Write([]byte(resultString))
	}
}
