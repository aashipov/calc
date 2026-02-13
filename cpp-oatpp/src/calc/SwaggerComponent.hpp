#ifndef calc_calc_SwaggerComponent_hpp
#define calc_calc_SwaggerComponent_hpp

#include "Constants.hpp"

#include "oatpp-swagger/Model.hpp"
#include "oatpp-swagger/Resources.hpp"
#include "oatpp/core/macro/component.hpp"

namespace calc {
namespace calc {

class SwaggerComponent {
public:
  /**
   *  General API docs info
   */
  OATPP_CREATE_COMPONENT(std::shared_ptr<oatpp::swagger::DocumentInfo>,
                         swaggerDocumentInfo)(Qualifiers::SERVICE_NAME, [] {
    oatpp::swagger::DocumentInfo::Builder builder;
    builder.setTitle("Calc Service")
        .setDescription("Calc Service")
        .setVersion("1.0")
        .addServer("http://0.0.0.0:8080", "server on localhost");
    return builder.build();
  }());

  /**
   *  Swagger-Ui Resources (<oatpp-examples>/lib/oatpp-swagger/res)
   */
  OATPP_CREATE_COMPONENT(std::shared_ptr<oatpp::swagger::Resources>,
                         swaggerResources)(Qualifiers::SERVICE_NAME, [] {
    // Make sure to specify correct full path to oatpp-swagger/res folder !!!
    return oatpp::swagger::Resources::streamResources(OATPP_SWAGGER_RES_PATH);
  }());
};

} // namespace calc
} // namespace calc

#endif // calc_calc_SwaggerComponent_hpp
