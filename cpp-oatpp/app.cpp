
#include "src/AppComponent.hpp"
#include "src/Runner.hpp"

void run() {

  /* Register Components in scope of run() method */
  calc::calc::AppComponent components({"localhost", 8080},
                                      {"calc.virtualhost", 0});

  /* run */
  std::list<std::thread> acceptingThreads;
  calc::calc::Runner runner;
  runner.run(acceptingThreads);

  for (auto &thread : acceptingThreads) {
    thread.join();
  }
}

int main(int argc, const char *argv[]) {

  oatpp::base::Environment::init();
  run();
  oatpp::base::Environment::destroy();

  return 0;
}
