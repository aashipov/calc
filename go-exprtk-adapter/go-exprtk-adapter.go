package go_exprtk_adapter

// #cgo CFLAGS: -O3 -I${SRCDIR}
// #cgo CXXFLAGS: -O3
// #cgo LDFLAGS: -L${SRCDIR}
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
