#include "CalcGetHandler.cpp"
#include "CalcPostHandler.cpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPRequestHandlerFactory.h"
#include "Poco/Net/HTTPServer.h"
#include "Poco/Net/HTTPServerParams.h"
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
