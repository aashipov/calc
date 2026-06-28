# Calculator service: Sanic flavor

Refer to [README.md](./README.md) for human-facing project details.

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - ad hoc throughput framework, which compares language-specific algebraic library to native library call.

## 1. Architecture & Structure

The service is composed of the following main components:

- `src/c_exprtk_adapter.py` - a `CDDL` adapter to `libc-exprtk-adapter` (see `../c-exprtk-adapter`)
- `src/app.py` - routes, main entry point (single file for the sake of brevity)
- `tests/` - Integration tests
- `build.sh` - Sets venv (./.venv/) up, builds a wheel & runs integration tests
- `run.sh` - to launch the app, perform load tests in a separate process and more

## 2. Dependencies & Setup

- **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

- `python` version 3 in `${PATH}`

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

## 4. Stylistics

- **Strict Typing**: Enforce Python type hints (PEP 484) on all function arguments and return signatures.
