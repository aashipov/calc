#include "CalcConstants.hpp"
#include "CalcRequestHandlerFactory.cpp"
#include "Poco/Net/HTTPServer.h"
#include <gtest/gtest.h>
#include <httplib.h>
#include <string>

class CalcTest : public ::testing::Test {

protected:
  const std::string EXPRESSION =
      "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
  const std::string EXPRESSION_RESULT =
      "19.9884328904852281993953511118888854980469";

  const std::string NOT_AN_EXPRESSION = "nan";

  Poco::Net::HTTPServer server = calc::buildHTTPServer(calc::SERVER_PORT);

  void SetUp() override { server.start(); }
  void TearDown() override { server.stop(); }

  void performRequest(std::string path, std::string requestBody,
                      std::string expected) {
    std::this_thread::sleep_for(std::chrono::milliseconds(10L));
    // tiny bit of time for HTTP server to start up
    httplib::Client cli("0.0.0.0", calc::SERVER_PORT);
    httplib::Result result;
    if (requestBody.empty()) {
      result = cli.Get(path);
    } else {
      result = cli.Post(path, requestBody, calc::TEXT_PLAIN);
    }
    EXPECT_EQ(result->body, expected);
  }
};

TEST_F(CalcTest, testWelcome) { performRequest("/", "", calc::WELCOME); }

TEST_F(CalcTest, testExpression) {
  performRequest("/", EXPRESSION, EXPRESSION_RESULT);
}

TEST_F(CalcTest, testNotAnExpression) {
  performRequest("/", NOT_AN_EXPRESSION, NOT_AN_EXPRESSION);
}
