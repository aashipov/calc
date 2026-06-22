#include "exprtk.hpp"

namespace calc {

template <typename T> inline exprtk::symbol_table<T> build_symbol_table() {
  static_assert(std::is_arithmetic_v<T>, "Template type must be arithmetic");
  exprtk::symbol_table<T> symbol_table;
  symbol_table.add_constants();
  symbol_table.add_constant("pi", exprtk::details::numeric::constant::pi);
  symbol_table.add_constant("e", exprtk::details::numeric::constant::e);
  return symbol_table;
};

template <typename T>
inline exprtk::symbol_table<T> &thread_local_symbol_table() {
  static thread_local exprtk::symbol_table<T> table = build_symbol_table<T>();
  return table;
}

template <typename T> inline exprtk::parser<T> &thread_local_parser() {
  static thread_local exprtk::parser<T> parser;
  return parser;
}

template <typename T> inline T calculate_inner(const char *expression) {
  exprtk::symbol_table<T> &symbol_table = thread_local_symbol_table<T>();
  exprtk::expression<T> exprtk_expression;
  exprtk_expression.register_symbol_table(symbol_table);

  exprtk::parser<T> &parser = thread_local_parser<T>();
  if (!parser.compile(expression, exprtk_expression)) {
    return std::numeric_limits<T>::quiet_NaN();
  }

  const T result = exprtk_expression.value();
  return result;
}

} // namespace calc

#ifdef __cplusplus
extern "C" {
#endif

#include "c-exprtk-adapter.h"

double calculate(const char *expression) {
  return calc::calculate_inner<double>(expression);
}

#ifdef __cplusplus
}
#endif
