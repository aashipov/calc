#include "CalcHandler.cpp"
#include "crow.h"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>

namespace calc {

static inline constexpr unsigned short HTTP_PORT = 8080;

crow::SimpleApp buildCalcApp(unsigned short httpPort) {
  crow::SimpleApp app;
  CROW_CATCHALL_ROUTE(app)([](const crow::request &req, crow::response &res) {
    calc::handler(req, res);
  });
  app.loglevel(crow::LogLevel::Error).port(httpPort);
  return app;
}

} // namespace calc
