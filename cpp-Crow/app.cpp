#include "crow.h"
#include "src/CalcServer.cpp"
#include <crow/app.h>
#include <crow/common.h>
#include <crow/http_response.h>

int main() {
  crow::SimpleApp app = calc::buildCalcApp(calc::HTTP_PORT);
  app.multithreaded().run();
}
