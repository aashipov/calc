#ifndef CPP_POCO_CALC_GET_HANDLER_H
#define CPP_POCO_CALC_GET_HANDLER_H

#include "CalcConstants.hpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPServerRequest.h"
#include "Poco/Net/HTTPServerResponse.h"

namespace calc {

class CalcGetHandler : public Poco::Net::HTTPRequestHandler {
public:
  void handleRequest(Poco::Net::HTTPServerRequest &request,
                     Poco::Net::HTTPServerResponse &response) override {
    response.setContentType(calc::TEXT_PLAIN);
    std::ostream &bodyStream = response.send();
    bodyStream << calc::WELCOME;
  }
};

} // namespace calc

#endif // CPP_POCO_CALC_GET_HANDLER_H
