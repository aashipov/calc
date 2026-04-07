#ifndef calc_calc_Runner_hpp
#define calc_calc_Runner_hpp

#include <list>
#include <thread>

#include "AppComponent.hpp"
#include "Constants.hpp"

#include "CalcController.hpp"

#include "oatpp-swagger/Controller.hpp"

#include "oatpp/network/Server.hpp"
#include "oatpp/web/server/HttpConnectionHandler.hpp"

namespace calc {
namespace calc {

class Runner {
public:
  void run() {
    oatpp::base::Environment::init();
    AppComponent components({"localhost", Qualifiers::HTTP_PORT},
                            {"calc.virtualhost", 0});
    std::list<std::thread> acceptingThreads;
    run(acceptingThreads);

    for (auto &thread : acceptingThreads) {
      thread.join();
    }
    oatpp::base::Environment::destroy();
  }

protected:
  void run(std::list<std::thread> &acceptingThreads) {
    OATPP_COMPONENT(std::shared_ptr<oatpp::web::server::HttpRouter>, router,
                    Qualifiers::SERVICE_NAME);

    oatpp::web::server::api::Endpoints docEndpoints;

    /* Add CalcController */
    docEndpoints.append(
        router->addController(std::make_shared<controller::CalcController>())
            ->getEndpoints());

    OATPP_COMPONENT(std::shared_ptr<oatpp::swagger::DocumentInfo>, documentInfo,
                    Qualifiers::SERVICE_NAME);
    OATPP_COMPONENT(std::shared_ptr<oatpp::swagger::Resources>, resources,
                    Qualifiers::SERVICE_NAME);

    router->addController(oatpp::swagger::Controller::createShared(
        docEndpoints, documentInfo, resources));

    /* Create connection handler */
    auto connectionHandler =
        oatpp::web::server::HttpConnectionHandler::createShared(router);

    acceptingThreads.push_back(std::thread([router, connectionHandler] {
      OATPP_COMPONENT(std::shared_ptr<oatpp::network::ServerConnectionProvider>,
                      connectionProvider, Qualifiers::SERVICE_NAME);
      oatpp::network::Server server(connectionProvider, connectionHandler);
      OATPP_LOGI("calc-service", "server is listening on port '%s'",
                 connectionProvider->getProperty("port").getData());
      server.run();
    }));

    acceptingThreads.push_back(std::thread([router, connectionHandler] {
      OATPP_COMPONENT(std::shared_ptr<oatpp::network::ServerConnectionProvider>,
                      connectionProvider, Qualifiers::SERVICE_VH);
      oatpp::network::Server server(connectionProvider, connectionHandler);
      OATPP_LOGI("calc-service",
                 "server is listening on virtual interface '%s'",
                 connectionProvider->getProperty("host").getData());
      server.run();
    }));
  }
};

} // namespace calc
} // namespace calc

#endif // calc_calc_Runner_hpp
