# Pure Go Calculator Service

This document provides a detailed technical overview of the pure Go algebraic computation service. The service is designed to accept algebraic expressions via HTTP and return computed results by routing requests to specialized, high-performance calculation engines selected based on workload requirements.

## File Structure

- `main.go`: Main entry point of the application.
- `exprtk.go`: Go bridge to `../c-exprtk-adapter` native library.
- `handler.go`: Implements the HTTP request handling logic and coordinates the calculation process.
- `main_test.go` / `handler_test.go` / `exprtk_test.go`: Unit, integration and end-to-end tests for the respective components.

## Building and Testing

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

## Run

### Development Run

See above

### Production Run

To execute the optimized production binary:

```bash
./app
```
