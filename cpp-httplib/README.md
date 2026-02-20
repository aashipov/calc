# cpp-httplib flavor

[cpp-httplib](https://github.com/yhirose/cpp-httplib) - 'blocking' socket I/O

## Requirements

Download the lib to user's include directory:

```shell
curl -L https://raw.githubusercontent.com/yhirose/cpp-httplib/refs/heads/master/httplib.h -o ${HOME}/.local/include/httplib.h
```

## Build

```shell
rm -rf build && mkdir build && cd build && cmake -DCMAKE_BUILD_TYPE=Release .. && make && cd ..
```

## Run

```shell
./build/calc-httplib
```
