#include "exprtk.hpp"

namespace calc {

template <typename T> static exprtk::symbol_table<T> build_symbol_table() {
  typedef exprtk::symbol_table<T> symbol_table_t;
  symbol_table_t symbol_table;
  symbol_table.add_constants();
  // symbol_table.add_constant("pi", exprtk::details::numeric::constant::pi);
  symbol_table.add_constant("e", exprtk::details::numeric::constant::e);
  return symbol_table;
}

template <typename T> static T calculate(std::string expression_string) {
  typedef exprtk::symbol_table<T> symbol_table_t;
  typedef exprtk::expression<T> expression_t;
  typedef exprtk::parser<T> parser_t;

  symbol_table_t symbol_table = build_symbol_table<T>();
  expression_t expression;
  expression.register_symbol_table(symbol_table);

  parser_t parser;
  parser.compile(expression_string, expression);
  const T result = expression.value();
  return result;
}

} // namespace calc
