#include "CalcHandler.hpp"

int main() {
  calc::buildApp(calc::SERVER_PORT);
  drogon::app().run();
  exit(EXIT_SUCCESS);
}
