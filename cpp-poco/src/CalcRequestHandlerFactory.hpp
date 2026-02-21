#ifndef CPP_POCO_CALC_REQUEST_HANDLER_FACTORY_H
#define CPP_POCO_CALC_REQUEST_HANDLER_FACTORY_H

#include "CalcGetHandler.hpp"
#include "CalcPostHandler.hpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPRequestHandlerFactory.h"
#include "Poco/Net/HTTPServer.h"
#include "Poco/Net/HTTPServerParams.h"
#include "Poco/Net/HTTPServerRequest.h"

namespace calc {

class CalcRequestHandlerFactory : public Poco::Net::HTTPRequestHandlerFactory {
public:
  Poco::Net::HTTPRequestHandler *
  createRequestHandler(const Poco::Net::HTTPServerRequest &request) {
    if (request.getMethod() == Poco::Net::HTTPServerRequest::HTTP_POST) {
      return new CalcPostHandler();
    } else {
      return new CalcGetHandler();
    }
  }
};

Poco::Net::HTTPServer buildHTTPServer(unsigned short httpPort) {
  unsigned int thread_count = std::thread::hardware_concurrency() * 2;
  Poco::ThreadPool::defaultPool().addCapacity(thread_count);
  Poco::Net::HTTPServerParams *httpServerParams =
      new Poco::Net::HTTPServerParams;
  httpServerParams->setMaxQueued(MAX_QUEUED);
  httpServerParams->setMaxThreads(thread_count);
  Poco::Net::ServerSocket serverSocket(httpPort);
  return Poco::Net::HTTPServer(new CalcRequestHandlerFactory(), serverSocket,
                               httpServerParams);
}

} // namespace calc

#endif // CPP_POCO_CALC_REQUEST_HANDLER_FACTORY_H
