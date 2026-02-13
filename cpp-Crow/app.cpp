#include "crow.h"
#include "exprtk-adapter.hpp"
#include <crow/common.h>
#include <crow/http_response.h>
#include <string>

static const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static const std::string CONTENT_TYPE = "Content-Type";

static const std::string TEXT_PLAIN = "text/plain";

int main() {
  crow::App<> app;
  CROW_CATCHALL_ROUTE(app)([](const crow::request &req, crow::response &res) {
    if (crow::HTTPMethod::POST == req.method) {
      std::string expression = req.body;
      double result = calc::calculate<double>(expression);
      res.body = std::to_string(result);
    } else {
      res.body = WELCOME;
    }
    res.code = 200;
    res.set_header(CONTENT_TYPE, TEXT_PLAIN);
    res.end();
  });
  app.loglevel(crow::LogLevel::Error);
  app.port(8080).multithreaded().run();
}
