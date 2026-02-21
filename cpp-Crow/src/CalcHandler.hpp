#ifndef CPP_CROW_CALC_HANDLER_H
#define CPP_CROW_CALC_HANDLER_H

#include "c-exprtk-adapter.h"
#include "crow.h"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>
#include <string>

namespace calc {

static inline const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static inline const std::string CONTENT_TYPE = "Content-Type";

static inline const std::string TEXT_PLAIN = "text/plain";

static inline const unsigned short DOUBLE_PRECISION = 40;

static inline std::string doubleToStringWithPrecision(double value,
                                                      int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}

static inline void handler(const crow::request &req, crow::response &res) {
  if (crow::HTTPMethod::POST == req.method) {
    std::string expression = req.body;
    double result = calculate(expression.c_str());
    res.body = doubleToStringWithPrecision(result, DOUBLE_PRECISION);
  } else {
    res.body = WELCOME;
  }
  res.code = 200;
  res.set_header(CONTENT_TYPE, TEXT_PLAIN);
  res.end();
}
} // namespace calc

#endif // CPP_CROW_CALC_HANDLER_H
