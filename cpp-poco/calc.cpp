#include "src/CalcServer.hpp"

int main(int argc, char **argv) {
  calc::CalcServer calcServer;
  return calcServer.run(argc, argv);
}
