# Calculator Service: Pure Bun flavor

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - ad hoc throughput framework, which compares language-specific algebraic library to native library call.

## 1. Architecture & Structure

The service is composed of the following main components:

- `calc.ts`: application entry point, HTTP request handler and expression evaluation facility.

## 2. Dependencies & Setup

- **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

- `Node.js 22+`, in `${PATH}`

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

If the request URI specifies `exprtk`, `../c-exprtk-adapter` handle the calculation.

Otherwise, fallback/default `MathJS` engine performs the computation.

## 4. Building and Testing

- **`build.sh`:** A utility script designed to function as a lightweight Continuous Integration environment.

## 4. Local Launch

```shell
./calc
```
