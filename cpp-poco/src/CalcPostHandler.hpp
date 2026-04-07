#ifndef CPP_POCO_CALC_POST_HANDLER_H
#define CPP_POCO_CALC_POST_HANDLER_H

#include "CalcConstants.hpp"
#include "Poco/Net/HTTPRequestHandler.h"
#include "Poco/Net/HTTPServerRequest.h"
#include "Poco/Net/HTTPServerResponse.h"
#include "c-exprtk-adapter.h"
#include <Poco/StreamCopier.h>
#include <iomanip>

namespace calc {

static inline std::string to_string(double value, int precision) {
  std::ostringstream ss;
  ss << std::fixed << std::setprecision(precision) << value;
  return ss.str();
}

class CalcPostHandler : public Poco::Net::HTTPRequestHandler {
public:
  void handleRequest(Poco::Net::HTTPServerRequest &request,
                     Poco::Net::HTTPServerResponse &response) override {
    std::string expression;
    Poco::StreamCopier::copyToString(request.stream(), expression);
    double result = calculate(expression.c_str());
    std::string result_string = to_string(result, DOUBLE_PRECISION);
    response.setContentType(calc::TEXT_PLAIN);
    std::ostream &responseBodyStream = response.send();
    responseBodyStream << result_string;
  }
};

} // namespace calc

#endif // CPP_POCO_CALC_POST_HANDLER_H
