
#ifndef calc_calc_Runner_hpp
#define calc_calc_Runner_hpp

#include "oatpp/web/server/api/ApiController.hpp"

#include <list>
#include <thread>

namespace calc {
namespace calc {

class Runner {
public:
  Runner();

  void run(std::list<std::thread> &acceptingThreads);
};

} // namespace calc
} // namespace calc

#endif // calc_calc_Runner_hpp
