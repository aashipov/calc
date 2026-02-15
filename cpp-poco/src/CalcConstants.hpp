#ifndef CALC_CONSTANTS_H
#define CALC_CONSTANTS_H
#include <string>

namespace calc {
inline const std::string TEXT_PLAIN = "text/plain";
inline const std::string WELCOME =
    "Welcome to calc service\nHTTP POST your expression";
inline const unsigned short SERVER_PORT = 8080;
inline const unsigned short MAX_QUEUED = 1000;
inline const unsigned short DOUBLE_PRECISION = 40;
} // namespace calc

#endif
