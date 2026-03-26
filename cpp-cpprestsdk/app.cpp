#include "src/CalcHandler.hpp"
#include "src/CalcServer.hpp"
#include <cpprest/http_listener.h>
#include <pplx/threadpool.h>

int main() {
  unsigned int thread_count = std::thread::hardware_concurrency();
  crossplat::threadpool::initialize_with_threads(thread_count);
  web::http::experimental::listener::http_listener listener =
      calc::buildCalcApp(calc::BASE_URL);
  try {
    listener.open().wait();
    std::string line;
    getline(std::cin, line);
  } catch (std::exception const &e) {
    std::cerr << e.what() << std::endl;
  }
  return EXIT_SUCCESS;
}
