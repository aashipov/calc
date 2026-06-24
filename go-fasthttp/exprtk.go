package main

/*
#cgo LDFLAGS: -lc-exprtk-adapter
#include "c-exprtk-adapter.h"
#include <stdlib.h>
*/
import "C"
import (
	"strconv"
	"unsafe"
)

// NaN represents an undefined result from ExprTk.
const (
	NaN = "NaN"
)

func CalculateViaExprtk(expression string) string {
	if expression == "" {
		return NaN
	}
	expressionCString := C.CString(expression)
	defer C.free(unsafe.Pointer(expressionCString))
	result := float64(C.calculate(expressionCString))
	return strconv.FormatFloat(result, 'f', -1, 64)
}
