# Calculator Service: C# flavor

Refer to [README.md](./README.md) for human-facing project details.

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - ad hoc throughput framework, which compares language-specific algebraic library to native library call.

## 1. Architecture & Structure

The service is composed of the following main components:

- `App.cs`: Main entry point of the application.
- `Calc.cs`: Implements the HTTP request handling logic and coordinates the calculation process.

## 2. Dependencies & Setup

- **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

- `dotnet`, in `${PATH}`

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

## 4. Building and Testing

```shell
dotnet build -c Release
```

### Testing

You can run all tests for the application:

```shell
dotnet test -c Release
```

## 5. Running the Service

Execute the optimized production binary.

```shell
Logging__LogLevel__Default=Warning ./bin/Release/net10.0/calc
```
