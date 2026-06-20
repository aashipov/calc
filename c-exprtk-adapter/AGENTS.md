# C exprtk adapter

Refer to [README.md](./README.md) for human-facing project details.

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) native library (adapter) for algebraic expression calculation.

## 1. Architecture & Structure

The adapter is composed of the following main components:

- `c-exprtk-adapter.h`: native library header file.
- `c-exprtk-adapter.cpp`: native library implementation.
- `exprtk-calculator/main.cpp`: showcase command line calculator implementation (optional).

## 2. Dependencies & Setup

Functioning C/C++ toolchain

`exprtk.hpp` on `${LIBRARY_PATH}`

Modern `make`, `cmake`

## 3. API Contract

Defined in `c-exprtk-adapter.h`

## 4. Building

### Release Configuration

The release build is optimized for production deployment, focusing on performance.

```shell
./release.sh
```

### Debug Configuration

The debug build is suited for development and debugging. It also performs a basic test via `exprtk-calculator`

```shell
./debug.sh
```
