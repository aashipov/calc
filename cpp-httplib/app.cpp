#include "exprtk-adapter.hpp"
#include "httplib.h"

namespace calc {

static const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static const std::string TEXT_PLAIN = "text/plain";

static void configure_server(httplib::Server &svr) {
  svr.Get(R"(/.*)",
          [](const httplib::Request & /*req*/, httplib::Response &res) {
            res.set_content(WELCOME, TEXT_PLAIN);
          });

  svr.Post(R"(/.*)", [](const httplib::Request &req, httplib::Response &res,
                        const httplib::ContentReader &content_reader) {
    std::string expr;
    content_reader([&](const char *data, size_t data_length) {
      expr.append(data, data_length);
      return true;
    });
    double result = calculate<double>(expr);
    res.set_content(std::to_string(result), TEXT_PLAIN);
  });
}

} // namespace calc

int main() {
  httplib::Server svr;
  calc::configure_server(svr);
  svr.listen("0.0.0.0", 8080);
  return EXIT_SUCCESS;
}
