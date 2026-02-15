#include "CalcGetHandler.cpp"
#include "CalcPostHandler.cpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPRequestHandlerFactory.h"
#include "Poco/Net/HTTPServerRequest.h"

using Poco::Net::HTTPRequestHandler;
using Poco::Net::HTTPRequestHandlerFactory;
using Poco::Net::HTTPServerRequest;

namespace calc {
class CalcRequestHandlerFactory : public HTTPRequestHandlerFactory {
public:
  HTTPRequestHandler *createRequestHandler(const HTTPServerRequest &request) {
    if (request.getMethod() == HTTPServerRequest::HTTP_POST) {
      return new CalcPostHandler();
    } else {
      return new CalcGetHandler();
    }
  }
};
} // namespace calc
