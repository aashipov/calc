#include "c-exprtk-adapter.h"
#include "httplib.h"

namespace calc {

static inline const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static inline const std::string TEXT_PLAIN = "text/plain";

static inline const unsigned short DOUBLE_PRECISION = 40;

static inline std::string doubleToStringWithPrecision(double value,
                                                      int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}

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
    double result = calculate(expr.c_str());
    std::string body = doubleToStringWithPrecision(result, DOUBLE_PRECISION);
    res.set_content(body, TEXT_PLAIN);
  });
}

} // namespace calc

int main() {
  httplib::Server svr;
  calc::configure_server(svr);
  svr.listen("0.0.0.0", 8080);
  return EXIT_SUCCESS;
}
