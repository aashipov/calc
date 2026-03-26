#include "crow.h"
#include "src/CalcServer.hpp"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>

int main() {
  unsigned int thread_count = 8 * std::thread::hardware_concurrency();
  crow::SimpleApp app = calc::buildCalcApp(calc::HTTP_PORT);
  app.concurrency(thread_count).run();
  return EXIT_SUCCESS;
}
