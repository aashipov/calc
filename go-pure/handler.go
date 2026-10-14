package main

import (
	"io"
	"net/http"
	"strings"
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
		path := r.URL.Path
		resultString := ""
		if strings.HasPrefix(path, "/meval") {
			resultString = CalculateViaMeval(expression)
		} else {
			resultString = CalculateViaExprtk(expression)
		}
		w.Write([]byte(resultString))
	}
}
