#include "crow.h"
#include "src/CalcServer.hpp"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>

int main() {
  crow::SimpleApp app = calc::build_calc_app();
  app.run();
  return EXIT_SUCCESS;
}
