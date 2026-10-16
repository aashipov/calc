package main

/*
#cgo LDFLAGS: -lc-exprtk-adapter -lc-meval-adapter
#include "c-exprtk-adapter.h"
#include "c-meval-adapter.h"
#include <stdlib.h>
*/
import "C"
import (
	"strconv"
	"unsafe"
)

func CalculateViaExprtk(expression string) string {
	expressionCString := C.CString(expression)
	defer C.free(unsafe.Pointer(expressionCString))
	result := float64(C.calculate_via_exprtk(expressionCString))
	return strconv.FormatFloat(result, 'f', -1, 64)
}

func CalculateViaMeval(expression string) string {
	expressionCString := C.CString(expression)
	defer C.free(unsafe.Pointer(expressionCString))
	result := float64(C.calculate_via_meval(expressionCString))
	return strconv.FormatFloat(result, 'f', -1, 64)
}
