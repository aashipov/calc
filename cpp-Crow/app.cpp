#include "c-exprtk-adapter.h"
#include "crow.h"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>
#include <string>

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

crow::SimpleApp buildCalcApp(unsigned short httpPort) {
  crow::SimpleApp app;
  CROW_CATCHALL_ROUTE(app)(
      [](const crow::request &req, crow::response &res) { handler(req, res); });
  app.loglevel(crow::LogLevel::Error).port(httpPort);
  return app;
}

int main() {
  crow::SimpleApp app = buildCalcApp(8080);
  app.multithreaded().run();
}
