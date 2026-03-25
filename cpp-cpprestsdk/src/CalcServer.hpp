#ifndef CPP_CPPRESTSDK_CALC_SERVER_H
#define CPP_CPPRESTSDK_CALC_SERVER_H

#include "CalcHandler.hpp"
#include <cpprest/http_listener.h>

namespace calc {

inline web::http::experimental::listener::http_listener
buildCalcApp(web::http::uri url) {
  web::http::experimental::listener::http_listener listener(url);
  listener.support(web::http::methods::GET, calc::get_handler);
  listener.support(web::http::methods::POST, calc::post_handler);
  return listener;
}

} // namespace calc
#endif // CPP_CPPRESTSDK_CALC_SERVER_H
