# Calculator Service: Rust Actix flavor

Refer to [README.md](./README.md) for human-facing project details.

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - ad hoc throughput framework, which compares language-specific algebraic library to native library call.

## 1. Architecture & Structure

The service is composed of the following main components:

- `c-exprtk-adapter.h`: `../c-exprtk-adapter/c-exprtk-adapter.h` copy, C‑ABI.
- `build.rs`: Runs `bindgen` on `c-exprtk-adapter.h` at compile time to create the Rust FFI bindings.
- `src/lib.rs`: Wraps the native call in a safe Rust API and also exposes a pure‑Rust evaluator (`meval`) for quick fallback.
- `src/handler.rs`: HTTP routing & request handling
- `src/api_docs.rs`: `utoipa::OpenApi` configuration
- `src/calc.rs`: main entry point

## 2. Dependencies & Setup

- **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

- Rust, C/C++ toolchain

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

## 4. Building and Testing

### Debug Configuration

Call `cargo build && cargo test` to test & build everything.

### Release Configuration

The release build is optimized for production deployment, focusing on performance. Launch `cargo build --release`.

## 5. Running the Service

### Development Run

```shell
./target/debug/calc
```

### Production Run

```shell
./target/release/calc
```
