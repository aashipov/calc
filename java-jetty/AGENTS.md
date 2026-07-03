# Calculator Service: Jetty flavor

This document provides a detailed technical overview of [C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter.

Main goal/purpose - ad hoc throughput framework, which compares language-specific algebraic library to native library call.

## 1. Architecture & Structure

The service is composed of the following main components:

- `App.java`: application entry point.
- `CalcServlet.java`: implements the HTTP request handling logic and coordinates the calculation process.
- `JavaExprtkAdapter.java`: high-performance native bridge (JNI/FFM) through the `../java-exprtk-adapter` and its native counterpart, `../c-exprtk-adapter`.

## 2. Dependencies & Setup

- **Native Library:** Requires the `../c-exprtk-adapter` native library to be present and correctly linked during the build process. Ensure the native library is compiled for the target architecture.

- `openJDK 25`, `maven` in `${PATH}`

## 3. API Contract

HTTP GET returns welcome message (healthcheck).

HTTP POST with expression to evaluate (plain text body) returns NaN or calculation result.

If the request URI specifies `mxparser`, the calculation is routed to the specialized `org-mXparser` engine.

If the request URI specifies `exprtk`, `../java-exprtk-adapter` and `../c-exprtk-adapter` handle the calculation.

Otherwise, fallback/default `com.udojava.EvalEx` engine performs the computation.

## 4. Building and Testing

- **`build.sh`:** A utility script designed to function as a lightweight Continuous Integration environment. It orchestrates validation checks for both the Java and native (JNI/FFM) components, including a full integration test driven by `curl`. _(Note: Setting `SHARED_LIBRARY_HARNESS` to `FFM` activates the FFM harness for execution.)_

Conventional `mvn clean package` will download dependencies, perform tests and build the runnable uber-jar.

## 4. Local Launch

The service can be launched in two modes:

1.  **JNI Mode:**
    ```shell
    java -jar target/calc-shaded.jar
    ```
2.  **FFM Mode:**
    ```shell
    SHARED_LIBRARY_HARNESS=FFM java -jar target/calc-shaded.jar
    ```
