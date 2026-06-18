package main

import (
	"io"
	"net/http"
)

const (
	TextPlain = "text/plain"
)

var (
	Welcome = []byte("Welcome to calc service\nHTTP POST your expression")
)

func CalcHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", TextPlain)
	if http.MethodGet == r.Method {
		w.Write(Welcome)
		return
	} else {
		bodyBytes, err := io.ReadAll(r.Body)
		r.Body.Close()
		if err != nil {
			w.Write([]byte(err.Error()))
			return
		}
		expression := string(bodyBytes)
		resultString := CalculateViaExprtk(expression)
		w.Write([]byte(resultString))
	}
}
