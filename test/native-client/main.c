#include "c-exprtk-adapter.h"
#include "rs-expr-adapter.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

double exprtk_time(const char *expr, long long max_cycles) {
  clock_t start = clock();
  for (long long i = 0; i < max_cycles; i++) {
    calculate_via_exprtk(expr);
  }
  clock_t finish = clock();
  return (double)(finish - start) / CLOCKS_PER_SEC;
}

double meval_time(const char *expr, long long max_cycles) {
  clock_t start = clock();
  for (long long i = 0; i < max_cycles; i++) {
    calculate_via_meval(expr);
  }
  clock_t finish = clock();
  return (double)(finish - start) / CLOCKS_PER_SEC;
}

void evaluate(const char *expr, long long max_cycles) {
  printf("Evaluating %s\n", expr);
  double result = calculate_via_exprtk(expr);
  printf("\texprtk:\t%lf\n", result);
  result = calculate_via_meval(expr);
  printf("\tmeval:\t%lf\n", result);
  printf("%s %lld cycles\n", expr, max_cycles);
  printf("\texprtk:\t%lf\n", exprtk_time(expr, max_cycles));
  printf("\tmeval:\t%lf\n", meval_time(expr, max_cycles));
}

int main() {
  long long MAX_CYCLES = 1000000;
  const char *expr = "2+2";
  evaluate(expr, MAX_CYCLES);
  expr = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
  evaluate(expr, MAX_CYCLES);
  return EXIT_SUCCESS;
}
