#include "c-exprtk-adapter.h"
#include <stdio.h>
#include <stdlib.h>

// gcc test.c -lc-exprtk-adapter -o test && ./test
int main() {
  char *expression_string =
      "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
  printf("%f\n", calculate(expression_string));
  printf("%f\n", calculate("abc"));
  return EXIT_SUCCESS;
}
