#include "exprtk.hpp"
#include "org_dummy_calc_ExprtkAdapter.h"

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

static std::string to_string(JNIEnv *env, jstring jstring) {
  jboolean isCopy;
  const char *value = (env)->GetStringUTFChars(jstring, &isCopy);
  return std::string(value);
}

JNIEXPORT jdouble JNICALL Java_org_dummy_calc_ExprtkAdapter_calculate(
    JNIEnv *env, jclass jclass, jstring jstring) {
  std::string expression = to_string(env, jstring);
  return calc::calculate<double>(expression);
}
