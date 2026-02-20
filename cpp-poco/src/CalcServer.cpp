#include "CalcConstants.hpp"
#include "CalcRequestHandlerFactory.cpp"
#include "Poco/Net/HTTPServer.h"
#include "Poco/Util/ServerApplication.h"

using Poco::Net::HTTPServer;
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
    HTTPServer httpServer = buildHTTPServer(SERVER_PORT);
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
