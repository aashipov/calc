#include "CalcConstants.hpp"
#include "CalcRequestHandlerFactory.cpp"
#include "Poco/Net/HTTPServer.h"
#include "Poco/Net/HTTPServerParams.h"
#include "Poco/Net/ServerSocket.h"
#include "Poco/ThreadPool.h"
#include "Poco/Util/ServerApplication.h"

using Poco::ThreadPool;
using Poco::Net::HTTPServer;
using Poco::Net::HTTPServerParams;
using Poco::Net::ServerSocket;
using Poco::Util::ServerApplication;

namespace calc {
class CalcServer : public ServerApplication {

public:
  CalcServer() {}
  ~CalcServer() {}

protected:
  void initialize(Application &self) {
    loadConfiguration();
    ServerApplication::initialize(self);
  }
  void uninitialize() { ServerApplication::uninitialize(); }

  int main(const std::vector<std::string> &args) {
      unsigned int thread_count = std::thread::hardware_concurrency() * 2;
    ThreadPool::defaultPool().addCapacity(thread_count);
    HTTPServerParams *httpServerParams = new HTTPServerParams;
    httpServerParams->setMaxQueued(MAX_QUEUED);
    httpServerParams->setMaxThreads(thread_count);

    ServerSocket serverSocket(SERVER_PORT);
    HTTPServer httpServer(new CalcRequestHandlerFactory(), serverSocket,
                          httpServerParams);
    // start the HTTPServer
    httpServer.start();
    // wait for CTRL-C or kill
    waitForTerminationRequest();
    // Stop the HTTPServer
    httpServer.stop();
    return Application::EXIT_OK;
  }
};
} // namespace calc
