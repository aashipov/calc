#include "c-exprtk-adapter.h"
#include <cstdlib>
#include <iomanip>
#include <iostream>
#include <ostream>

static inline const unsigned short DOUBLE_PRECISION = 40;

static inline std::string doubleToStringWithPrecision(double value,
                                                      int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}

int main(int argc, char *argv[]) {
  if (argc >= 2) {
    char *expression_string = argv[1];
    double result = calculate(expression_string);
    std::cout << doubleToStringWithPrecision(result, DOUBLE_PRECISION)
              << std::endl;
  } else {
    std::cout << "Usage:\n exprtk-calculator "
                 "\"(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2\""
              << std::endl;
  }
  EXIT_SUCCESS;
}
