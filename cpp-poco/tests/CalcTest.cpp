#include "CalcConstants.hpp"
#include "CalcRequestHandlerFactory.cpp"
#include "Poco/Net/HTTPClientSession.h"
#include "Poco/Net/HTTPRequest.h"
#include "Poco/Net/HTTPResponse.h"
#include "Poco/Net/HTTPServer.h"
#include "Poco/Net/HTTPServerParams.h"
#include "Poco/Net/ServerSocket.h"
#include <gtest/gtest.h>
#include <string>

class CalcTest : public ::testing::Test {

protected:
  const std::string EXPRESSION =
      "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
  const std::string EXPRESSION_RESULT = "19.9884328904852281993953511118888854980469";

  const std::string NOT_AN_EXPRESSION = "nan";

  Poco::Net::HTTPServer *_server;

  void SetUp() override {
    Poco::Net::ServerSocket svs(0);
    Poco::Net::HTTPServerParams *pParams = new Poco::Net::HTTPServerParams;
    pParams->setKeepAlive(false);
    _server = new Poco::Net::HTTPServer(new calc::CalcRequestHandlerFactory,
                                        svs, pParams);
    _server->start();
  }
  void TearDown() override {
    _server->stop();
    delete _server;
  }
  void performRequest(Poco::Net::HTTPRequest *request, std::string requestBody,
                      std::string expected) {
    Poco::Net::HTTPClientSession clientSession(
        "127.0.0.1", _server->socket().address().port());
    request->setContentType(calc::TEXT_PLAIN);
    if (requestBody.empty()) {
      clientSession.sendRequest(*request);
    } else {
      request->setContentLength((int)requestBody.length());
      clientSession.sendRequest(*request) << requestBody;
    }
    Poco::Net::HTTPResponse response;
    std::string responseBody;
    std::istream &inputStream = clientSession.receiveResponse(response);
    Poco::StreamCopier::copyToString(inputStream, responseBody);
    EXPECT_EQ(response.getStatus(), Poco::Net::HTTPResponse::HTTP_OK);
    EXPECT_EQ(response.getContentType(), calc::TEXT_PLAIN);
    EXPECT_EQ(responseBody, expected);
  }
};

TEST_F(CalcTest, testWelcome) {
  Poco::Net::HTTPRequest *request = new Poco::Net::HTTPRequest("GET", "/");
  performRequest(request, "", calc::WELCOME);
  delete request;
}

TEST_F(CalcTest, testExpression) {
  Poco::Net::HTTPRequest *request = new Poco::Net::HTTPRequest("POST", "/");
  performRequest(request, EXPRESSION, EXPRESSION_RESULT);
  delete request;
}

TEST_F(CalcTest, testNotAnExpression) {
  Poco::Net::HTTPRequest *request = new Poco::Net::HTTPRequest("POST", "/");
  performRequest(request, NOT_AN_EXPRESSION, NOT_AN_EXPRESSION);
  delete request;
}
