# POCO C++ flavor

## Requirements

Build `../c-exprtk-adapter`, `../test/build-googletest.sh` first

For tests, download the lib to user's include directory:

```shell
curl -L https://raw.githubusercontent.com/yhirose/cpp-httplib/refs/heads/master/httplib.h -o ${HOME}/.local/include/httplib.h
```

## Build

```shell
./build.sh
```

## Run

```shell
./build/calc
```
