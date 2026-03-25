# Crow flavor

## Requirements

[cpprestsdk](https://github.com/Microsoft/cpprestsdk/) installed, e.g. as a [package](https://aur.archlinux.org/cpprestsdk.git) (needs patching for modern Boost & toolchain)

Build `../c-exprtk-adapter` first

For tests, download the lib to user's include directory:

```shell
curl -L https://raw.githubusercontent.com/yhirose/cpp-httplib/refs/heads/master/httplib.h -o ${HOME}/.local/include/httplib.h
```

## Build

### Release

```shell
./release.sh
```

## Run

```shell
./build/calc
```
