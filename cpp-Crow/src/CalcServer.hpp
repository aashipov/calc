#ifndef CPP_CROW_CALC_SERVER_H
#define CPP_CROW_CALC_SERVER_H

#include "CalcHandler.hpp"
#include "crow.h"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>

namespace calc {

static inline constexpr unsigned short HTTP_PORT = 8080;

inline crow::SimpleApp
build_calc_app(unsigned short httpPort = HTTP_PORT,
               unsigned int threadCount =
                   std::max(2, (int)std::thread::hardware_concurrency())) {
  crow::SimpleApp app;
  CROW_CATCHALL_ROUTE(app)([](const crow::request &req, crow::response &res) {
    calc::handler(req, res);
  });
  app.loglevel(crow::LogLevel::Error).port(httpPort);
  app.concurrency(threadCount);
  return app;
}

} // namespace calc

#endif // CPP_CROW_CALC_SERVER_H
