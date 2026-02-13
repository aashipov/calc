package go_exprtk_adapter

// #cgo CFLAGS: -I${SRCDIR}
// #include "go-exprtk-adapter.h"
// #include <stdlib.h>
import "C"
import (
	"unsafe"
)

func Evaluate(expression string) float64 {
	cExpression := C.CString(expression)
	defer C.free(unsafe.Pointer(cExpression))
	return float64(C.LIB_evaluate(cExpression))
}
