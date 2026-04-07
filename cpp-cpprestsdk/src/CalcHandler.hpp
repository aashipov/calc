#ifndef CPP_CPPRESTSDK_CALC_HANDLER_H
#define CPP_CPPRESTSDK_CALC_HANDLER_H

#include "c-exprtk-adapter.h"
#include <cpprest/http_listener.h>
#include <iomanip>

namespace calc {

static inline const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";
static inline const unsigned short DOUBLE_PRECISION = 40;
const utility::string_t BASE_URL = U("http://0.0.0.0:8080");
static inline constexpr unsigned short HTTP_PORT = 8080;
static inline const std::string TEXT_PLAIN = "text/plain";

static inline std::string to_string(double value, int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}

static inline std::string via_exprtk(std::string expr) {
  double result = calculate(expr.c_str());
  return to_string(result, DOUBLE_PRECISION);
}

inline void get_handler(web::http::http_request request) {
  request.reply(web::http::status_codes::OK, WELCOME, TEXT_PLAIN);
}

inline void post_handler(web::http::http_request request) {
  request.extract_string()
      .then([](utility::string_t expr) { return via_exprtk(expr); })
      .then([=](std::string result) {
        request.reply(web::http::status_codes::OK, result, TEXT_PLAIN);
      });
}

} // namespace calc
#endif // CPP_CPPRESTSDK_CALC_HANDLER_H
