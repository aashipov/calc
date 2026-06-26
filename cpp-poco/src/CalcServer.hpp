#ifndef CPP_POCO_CALC_SERVER_H
#define CPP_POCO_CALC_SERVER_H

#include "CalcConstants.hpp"
#include "CalcRequestHandlerFactory.hpp"
#include "Poco/Net/HTTPServer.h"
#include "Poco/Util/ServerApplication.h"

namespace calc {
class CalcServer : public Poco::Util::ServerApplication {

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
    Poco::Net::HTTPServer httpServer = build_http_server();
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

#endif // CPP_POCO_CALC_SERVER_H
