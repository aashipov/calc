#pragma once

#ifdef __cplusplus
extern "C" {
#endif

[[deprecated("Use calculate_via_exprtk() instead")]]
double calculate(const char *expression);

double calculate_via_exprtk(const char *expression);

#ifdef __cplusplus
}
#endif
