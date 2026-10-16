#include "c-exprtk-adapter.h"
#include "exprtk.hpp"

#include <limits>
#include <type_traits>

namespace calc {

template <typename T> inline exprtk::symbol_table<T> build_symbol_table() {
  static_assert(std::is_arithmetic_v<T>, "Template type must be arithmetic");
  exprtk::symbol_table<T> symbol_table;
  symbol_table.add_constants();
  symbol_table.add_constant("pi", exprtk::details::numeric::constant::pi);
  symbol_table.add_constant("e", exprtk::details::numeric::constant::e);
  return symbol_table;
};

template <typename T> struct thread_local_context {
  exprtk::symbol_table<T> symbol_table = build_symbol_table<T>();
  exprtk::expression<T> expression;
  exprtk::parser<T> parser;
};

template <typename T> inline thread_local_context<T> &build_context() {
  static thread_local thread_local_context<T> ctx;
  ctx.expression.register_symbol_table(ctx.symbol_table);
  return ctx;
}

bool is_null_or_blank_c_string(const char *str) {
  // 1. Safety check for null
  if (!str) {
    return true;
  }

  // 2. Check for empty string "" or advance past whitespace
  while (*str != '\0') {
    if (!std::isspace(static_cast<unsigned char>(*str))) {
      return false; // Found a valid visible character
    }
    str++;
  }

  return true; // Iterated through the whole string and found only spaces or
               // '\0'
}

template <typename T>
[[nodiscard]] inline T calculate_inner(const char *expression) {
  if (is_null_or_blank_c_string(expression)) {
    double my_nan = std::numeric_limits<double>::quiet_NaN();
    return std::numeric_limits<T>::quiet_NaN();
  }
  try {
    thread_local_context<T> &ctx = build_context<T>();
    if (!ctx.parser.compile(expression, ctx.expression)) {
      return std::numeric_limits<T>::quiet_NaN();
    }
    return ctx.expression.value();
  } catch (...) {
    return std::numeric_limits<T>::quiet_NaN();
  }
}

} // namespace calc

/* C wrapper -------------------------------------------------------------- */

extern "C" {

double calculate(const char *expression) {
  return calc::calculate_inner<double>(expression);
}

double calculate_via_exprtk(const char *expression) {
  return calc::calculate_inner<double>(expression);
}

} // extern "C"
