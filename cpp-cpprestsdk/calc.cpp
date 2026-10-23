#include "src/CalcServer.hpp"
#include <cpprest/http_listener.h>

int main() {
  web::http::experimental::listener::http_listener listener =
      calc::build_calc_app();
  try {
    listener.open().wait();
    std::string line;
    getline(std::cin, line);
  } catch (std::exception const &e) {
    std::cerr << e.what() << std::endl;
  }
  return EXIT_SUCCESS;
}
