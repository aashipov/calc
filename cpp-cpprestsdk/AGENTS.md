# Calculator Service: C++ REST SDK

Refer to [README.md](./README.md) for human-facing project details.

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - part of ad hoc throughput framework (language-specific algebraic library to native library call cost comparison).

## 1. Architecture & Structure

The service is composed of the following main components:

- `app.cpp`: main entry point of the application.
- `src/CalcServer.hpp`: an application launcher class.
- `src/CalcHandler.hpp`: application logic
- `tests/CalcTest.cpp` minimalistic test coverage

## 2. Dependencies & Setup

- Functioning C/C++ toolchain

- Modern `make`, `cmake`, `C++ REST SDK`, `Google Test`

- **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

## 4. Building and Testing

Call `release.sh` to build & testeverything with `Release` type

## 5. Running the Service

### Production Run

```bash
./Release/calc
```
