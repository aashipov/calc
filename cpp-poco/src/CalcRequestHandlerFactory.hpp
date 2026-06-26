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
  createRequestHandler(const Poco::Net::HTTPServerRequest &request) override {
    if (request.getMethod() == Poco::Net::HTTPServerRequest::HTTP_POST) {
      return new CalcPostHandler;
    } else {
      return new CalcGetHandler;
    }
  }
};

inline Poco::Net::HTTPServer build_http_server(
    unsigned short httpPort = HTTP_PORT,
    unsigned int threadCount = std::max(2, (int)std::thread::hardware_concurrency())) {
  Poco::ThreadPool *tp = new Poco::ThreadPool;
  tp->addCapacity(threadCount);
  Poco::Net::HTTPServerParams *httpServerParams =
      new Poco::Net::HTTPServerParams;
  httpServerParams->setMaxQueued(MAX_QUEUED);
  httpServerParams->setMaxThreads(threadCount);
  Poco::Net::ServerSocket serverSocket(httpPort);
  return Poco::Net::HTTPServer(new CalcRequestHandlerFactory, *tp, serverSocket,
                               httpServerParams);
}

} // namespace calc

#endif // CPP_POCO_CALC_REQUEST_HANDLER_FACTORY_H
