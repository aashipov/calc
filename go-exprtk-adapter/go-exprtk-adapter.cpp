#include "go-exprtk-adapter.h"
#include "exprtk-adapter.hpp"

double LIB_evaluate(const char *expression) {
  std::string expression_string(expression);
  double result = calc::calculate<double>(expression_string);
  return result;
}
