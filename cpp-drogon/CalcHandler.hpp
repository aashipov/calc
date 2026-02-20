#ifndef CALC_HANDLER_H
#define CALC_HANDLER_H

#include "c-exprtk-adapter.h"
#include <drogon/HttpTypes.h>
#include <drogon/drogon.h>
#include <iomanip>
#include <string>
#include <trantor/utils/Logger.h>

namespace calc {

static const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static const std::string TEXT_PLAIN = "text/plain";

static const unsigned short DOUBLE_PRECISION = 40;

static const unsigned short SERVER_PORT = 8080;

static inline std::string doubleToStringWithPrecision(double value,
                                                      int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}

void buildApp(unsigned short httpPort) {
  drogon::app().registerHandler(
      "/",
      [](const drogon::HttpRequestPtr &request,
         std::function<void(const drogon::HttpResponsePtr &)> &&callback) {
        drogon::HttpResponsePtr resp = drogon::HttpResponse::newHttpResponse();
        resp->setStatusCode(drogon::HttpStatusCode::k200OK);
        resp->setContentTypeCode(drogon::ContentType::CT_TEXT_PLAIN);
        resp->setBody(WELCOME);
        callback(resp);
      },
      {drogon::Get});

  drogon::app().registerHandler(
      "/",
      [](const drogon::HttpRequestPtr &request,
         std::function<void(const drogon::HttpResponsePtr &)> &&callback) {
        std::string expression(request->getBody());
        double result = calculate(expression.c_str());
        drogon::HttpResponsePtr resp = drogon::HttpResponse::newHttpResponse();
        resp->setStatusCode(drogon::HttpStatusCode::k200OK);
        resp->setContentTypeCode(drogon::ContentType::CT_TEXT_PLAIN);
        resp->setBody(doubleToStringWithPrecision(result, DOUBLE_PRECISION));
        callback(resp);
      },
      {drogon::Post});

  LOG_INFO << "Server running";
  drogon::app()
      .addListener("0.0.0.0", httpPort)
      .setThreadNum(std::thread::hardware_concurrency());
}

} // namespace calc

#endif // CALC_HANDLER_H
