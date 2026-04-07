#include "crow.h"
#include "src/CalcServer.hpp"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>

int main() {
  unsigned int thread_count =
      std::max(2, (int)std::thread::hardware_concurrency());
  crow::SimpleApp app = calc::build_calc_app(calc::HTTP_PORT);
  app.concurrency(thread_count).run();
  return EXIT_SUCCESS;
}
