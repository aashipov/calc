package main

// #cgo LDFLAGS: -lc-exprtk-adapter
// #include "c-exprtk-adapter.h"
// #include <stdlib.h>
import "C"
import (
	"strconv"
	"unsafe"
)

func CalculateViaExprtk(expression string) string {
	expressionCString := C.CString(expression)
	defer C.free(unsafe.Pointer(expressionCString))
	result := float64(C.calculate(expressionCString))
	return strconv.FormatFloat(result, 'f', -1, 64)
}