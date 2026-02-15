#include "CalcConstants.hpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPServerRequest.h"
#include "Poco/Net/HTTPServerResponse.h"

using Poco::Net::HTTPRequestHandler;
using Poco::Net::HTTPServerRequest;
using Poco::Net::HTTPServerResponse;

namespace calc {
class CalcGetHandler : public HTTPRequestHandler {
public:
  void handleRequest(HTTPServerRequest &request, HTTPServerResponse &response) {
    response.setContentType(calc::TEXT_PLAIN);
    std::ostream &bodyStream = response.send();
    bodyStream << calc::WELCOME;
  }
};
} // namespace calc
