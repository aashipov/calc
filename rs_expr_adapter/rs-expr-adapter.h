#ifndef RS_EXPR_ADAPTER_H
#define RS_EXPR_ADAPTER_H

#include <cstdarg>
#include <cstdint>
#include <cstdlib>
#include <new>
#include <ostream>

#ifdef __cplusplus
extern "C" {
#endif

double via_meval(const char *expr);

double via_exprtk(const char *expr);

#ifdef __cplusplus
}
#endif

#endif // RS_EXPR_ADAPTER_H
