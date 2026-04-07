#include "CalcServer.hpp"
#include "crow.h"
#include <chrono>
#include <crow/app.h>
#include <gtest/gtest.h>
#include <httplib.h>
#include <pthread.h>
#include <string>
#include <thread>

class CalcTest : public ::testing::Test {

protected:
  inline static const std::string SIMPLE_EXPRESSION = "2+2";
  inline static const std::string SIMPLE_EXPRESSION_RESULT =
      "4.0000000000000000000000000000000000000000";

  inline static const std::string COMPLEX_EXPRESSION =
      "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
  inline static const std::string COMPLEX_EXPRESSION_RESULT =
      "19.9884328904852281993953511118888854980469";

  inline static const std::string NOT_AN_EXPRESSION = "nan";

  crow::SimpleApp server = calc::build_calc_app(calc::HTTP_PORT);
  // pristine server.run_async() or wrapped into std::async(...) block forever
  std::thread server_thread = std::thread([&]() { server.run(); });

  void TearDown() override {
    server.stop();
    server_thread.join();
  }

  void do_request(std::string path, std::string requestBody,
                  std::string expected) {
    // tiny bit of time for HTTP server to start up
    std::this_thread::sleep_for(std::chrono::milliseconds(10L));
    httplib::Client cli("0.0.0.0", calc::HTTP_PORT);
    httplib::Result result;
    if (requestBody.empty()) {
      result = cli.Get(path);
    } else {
      result = cli.Post(path, requestBody, calc::TEXT_PLAIN);
    }
    EXPECT_EQ(result->body, expected);
  }
};

GTEST_TEST_F(CalcTest, test_welcome) { do_request("/", "", calc::WELCOME); }

GTEST_TEST_F(CalcTest, test_simple_expression) {
  do_request("/", SIMPLE_EXPRESSION, SIMPLE_EXPRESSION_RESULT);
}

GTEST_TEST_F(CalcTest, test_complex_expression) {
  do_request("/", COMPLEX_EXPRESSION, COMPLEX_EXPRESSION_RESULT);
}

GTEST_TEST_F(CalcTest, test_invalid_expression) {
  do_request("/", NOT_AN_EXPRESSION, NOT_AN_EXPRESSION);
}
