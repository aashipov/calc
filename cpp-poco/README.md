# POCO C++ flavor

## Requirements

Build `../c-exprtk-adapter`, `../test/build-googletest.sh` first

## Build

### Debug with tests

```shell
rm -rf build && mkdir build && cd build && cmake -DENABLE_TESTS=ON -DCMAKE_BUILD_TYPE=Debug .. && make && ctest --test-dir ./tests/ && cd ..
```

or:

```shell
rm -rf build && mkdir build && cmake -DENABLE_TESTS=ON -S . -B build && cmake --build build && cd build && ctest --test-dir ./tests/ && cd ..
```

### Release

```shell
rm -rf build && mkdir build && cd build && cmake -DCMAKE_BUILD_TYPE=Release .. && make && cd ..
```

## Run

```shell
./build/calc-poco
```
