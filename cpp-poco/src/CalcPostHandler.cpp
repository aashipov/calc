#include "CalcConstants.hpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPServerRequest.h"
#include "Poco/Net/HTTPServerResponse.h"
#include "c-exprtk-adapter.h"
#include <Poco/StreamCopier.h>
#include <iomanip>

using Poco::Net::HTTPRequestHandler;
using Poco::Net::HTTPServerRequest;
using Poco::Net::HTTPServerResponse;

namespace calc {
static inline std::string doubleToStringWithPrecision(double value,
                                                      int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}
class CalcPostHandler : public HTTPRequestHandler {
public:
  void handleRequest(HTTPServerRequest &request, HTTPServerResponse &response) {
    std::string expression;
    Poco::StreamCopier::copyToString(request.stream(), expression);
    const char *expression_c_string = expression.c_str();
    double result = calculate(expression_c_string);
    std::string result_string =
        doubleToStringWithPrecision(result, DOUBLE_PRECISION);
    response.setContentType(calc::TEXT_PLAIN);
    std::ostream &responseBodyStream = response.send();
    responseBodyStream << result_string;
  }
};
} // namespace calc
