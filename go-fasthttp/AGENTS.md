# Calculator Service: Go FastHTTP flavor

Refer to [README.md](./README.md) for human-facing project details.

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - ad hoc throughput framework, which compares language-specific algebraic library to native library call. 

## 1. Architecture & Structure

The service is composed of the following main components:

- `main.go`: Main entry point of the application.
- `handler.go`: Implements the HTTP request handling logic and coordinates the calculation process.
- `exprtk.go`: Go bridge to `../c-exprtk-adapter` native library.

## 2. Dependencies & Setup

*   **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

* `go`, `gcc-go` in `${PATH}`

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

## 4. Building and Testing

Call `make` to test & build everything or follow paragraphs below

### Testing

You can run all tests for the application:

```shell
go test
```

### Build: Debug Configuration

The debug build is optimized for rapid iteration and remote inspection using Delve (`dlv`).

1.  **Build:** Compile the application with disabled optimizations (`-N -l`).
    ```shell
    go build -gcflags "all=-N -l" -o app-debug
    ```
2.  **Run (Remote Debugging):** Start the service and open the remote debugger port.
    ```shell
    dlv exec --listen=:2345 --headless --api-version=2 --accept-multiclient --continue ./app-debug
    ```
    _Use your preferred IDE (like VS Code or Zed) to connect to `localhost:2345` to set breakpoints._

### Build: Release Configuration

The release build is optimized for production deployment, focusing on performance.

1.  **Build:** Compile the production-ready binary.
    ```shell
    go build -o app
    ```

## 5. Running the Service

### Development Run

Run the debug binary for local development and debugging. (See Build: Debug Configuration for command details).

### Production Run

Execute the optimized production binary.

```bash
./app
```
