#ifndef calc_calc_CalcController_hpp
#define calc_calc_CalcController_hpp

#include "calc/exprtk-adapter.hpp"

#include "calc/Constants.hpp"

#include "oatpp/core/macro/codegen.hpp"
#include "oatpp/core/macro/component.hpp"
#include "oatpp/parser/json/mapping/ObjectMapper.hpp"
#include "oatpp/web/server/api/ApiController.hpp"

namespace calc {
namespace calc {
namespace controller {

static const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

static const std::string TEXT_PLAIN = "text/plain";

#include OATPP_CODEGEN_BEGIN(ApiController) //<--- Codegen begin

class CalcController : public oatpp::web::server::api::ApiController {

public:
  CalcController(
      OATPP_COMPONENT(std::shared_ptr<ObjectMapper>, objectMapper,
                      Qualifiers::SERVICE_NAME) /* Inject object mapper */)
      : oatpp::web::server::api::ApiController(objectMapper) {}

public:
  ENDPOINT_INFO(welcome) {
    info->summary = "Welcome";
    info->addResponse<String>(Status::CODE_200, TEXT_PLAIN);
  }
  ENDPOINT("GET", "/", welcome) {
    return createResponse(Status::CODE_200, WELCOME);
  }

  ENDPOINT_INFO(evaluate) {
    info->summary = "Evaluate expression";
    info->addConsumes<String>(TEXT_PLAIN);
    info->addResponse<String>(Status::CODE_200, TEXT_PLAIN);
  }
  ENDPOINT("POST", "/", evaluate, BODY_STRING(String, body)) {
    double result = calculate<double>(body);

    return createResponse(Status::CODE_200, std::to_string(result));
  }
};

#include OATPP_CODEGEN_END(ApiController) //<--- Codegen end

} // namespace controller
} // namespace calc
} // namespace calc

#endif // calc_calc_CalcController_hpp
