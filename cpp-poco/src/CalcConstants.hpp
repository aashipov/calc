#ifndef CPP_POCO_CALC_CONSTANTS_H
#define CPP_POCO_CALC_CONSTANTS_H

#include <string>

namespace calc {

inline const std::string TEXT_PLAIN = "text/plain";

inline const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";

inline const unsigned short HTTP_PORT = 8080;

inline const unsigned short MAX_QUEUED = 10000;

inline const unsigned short DOUBLE_PRECISION = 40;

} // namespace calc

#endif // CPP_POCO_CALC_CONSTANTS_H
