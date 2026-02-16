package main

// #cgo LDFLAGS: -lc-exprtk-adapter
// #include "c-exprtk-adapter.h"
// #include <stdlib.h>
import "C"

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"syscall"
	"unsafe"
)

var (
	httpPort = "8080"
	welcome  = []byte("Welcome to calc service\nHTTP POST your expression")
)

func handler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	if "GET" == r.Method {
		w.Write(welcome)
		return
	} else {
		bodyBytes, err := io.ReadAll(r.Body)
		if err != nil {
			return
		}
		expression := string(bodyBytes)
		expressionCString := C.CString(expression)
		defer C.free(unsafe.Pointer(expressionCString))
		result := float64(C.calculate(expressionCString))
		resultString := strconv.FormatFloat(result, 'f', -1, 64)
		w.Write([]byte(resultString))
	}

}

func enableGracefulShutdown(server *http.Server) {
	gracefulShutdown := make(chan os.Signal, 1)
	signal.Notify(gracefulShutdown, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		sig := <-gracefulShutdown
		log.Printf("%s received, shutdown", sig)
		server.Close()
		os.Exit(0)
	}()
}

func main() {
	http.HandleFunc("/", handler)
	server := http.Server{Addr: ":" + httpPort, Handler: nil}
	enableGracefulShutdown(&server)
	fmt.Println("Starting server on :" + httpPort)
	err := server.ListenAndServe()
	if err != nil {
		log.Fatal(err)
	}
}
