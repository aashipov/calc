#include "exprtk-adapter.hpp"
#include <drogon/HttpTypes.h>
#include <drogon/drogon.h>
#include <string>
#include <trantor/utils/Logger.h>

static const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static const std::string TEXT_PLAIN = "text/plain";

int main() {
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
        double result = calc::calculate<double>(expression);
        drogon::HttpResponsePtr resp = drogon::HttpResponse::newHttpResponse();
        resp->setStatusCode(drogon::HttpStatusCode::k200OK);
        resp->setContentTypeCode(drogon::ContentType::CT_TEXT_PLAIN);
        resp->setBody(std::to_string(result));
        callback(resp);
      },
      {drogon::Post});

  LOG_INFO << "Server running";
  drogon::app()
      .addListener("0.0.0.0", 8080)
      .setThreadNum(std::thread::hardware_concurrency())
      .run();
  exit(EXIT_SUCCESS);
}
