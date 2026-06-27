#ifndef CPP_CPPRESTSDK_CALC_SERVER_H
#define CPP_CPPRESTSDK_CALC_SERVER_H

#include "CalcHandler.hpp"
#include <cpprest/http_listener.h>
#include <pplx/threadpool.h>

namespace calc {

inline web::http::experimental::listener::http_listener
build_calc_app(web::http::uri url = BASE_URL) {
  unsigned int thread_count =
      std::max(2u, (uint)std::thread::hardware_concurrency());
  crossplat::threadpool::initialize_with_threads(thread_count);
  web::http::experimental::listener::http_listener listener(url);
  listener.support(web::http::methods::GET, calc::get_handler);
  listener.support(web::http::methods::POST, calc::post_handler);
  return listener;
}

} // namespace calc
#endif // CPP_CPPRESTSDK_CALC_SERVER_H
