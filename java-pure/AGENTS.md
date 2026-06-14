# Pure Java Calculator Service

This document provides a detailed technical overview of the pure Java algebraic computation service. The service is designed to accept algebraic expressions via HTTP and return computed results by routing requests to specialized, high-performance calculation engines selected based on workload requirements.

## 1. Technology Stack and Dependencies

The service utilizes a diverse stack of components to manage HTTP communication, dependency resolution, and robust, specialized computation.

- **HTTP Server:** Leverages `com.sun.net.httpserver.HttpServer` for lightweight, native handling of inbound HTTP requests.
- **Computation Engines (Specialization Strategy):** The service employs a specialized engine routing strategy, selecting the most appropriate engine for the given calculation:
  - **`exprtk` (Native/High-Performance):** This engine is utilized for optimal speed and complex algebraic evaluation. It is accessed via a high-performance native bridge (JNI/FFM) through the `../java-exprtk-adapter` and its native counterpart, `../c-exprtk-adapter`. This strategy allows for offloading heavy computation to optimized C/C++ code, minimizing Java overhead.
  - **`org-mXparser` (Specialized/Intensive):** Reserved for highly specialized or exceptionally complex calculation sets, this engine is noted for its significant resource consumption.
  - **`com.udojava.EvalEx` (General Purpose/Fallback):** Serves as a reliable general-purpose evaluator and is the default fallback mechanism when no specialized engine is explicitly requested.

## 2. System Architecture and Components

Maven manages dependency resolution and the overall build lifecycle. The core business logic is structured within the `org.dummy.calc` package.

### Application Layer (`org.dummy.calc`)

- **`App` (Bootstrapper):** The application's entry point. This class initializes and encapsulates the `com.sun.net.httpserver.HttpServer` instance, serving as the primary service bootstrapper.
- **`CalcHandler` (Request Router):** Implements `com.sun.net.httpserver.HttpHandler`. This component acts as the centralized request router, managing all incoming GET and POST requests:
  - **GET Requests:** Always responds with a defined `WELCOME` status, serving as an API health check endpoint.
  - **POST Requests (Routing Logic):** The router analyzes the URI context to determine the required computation:
    - If the URI specifies `mxparser`, the calculation is routed to the specialized `org-mXparser` engine.
    - If the URI specifies `exprtk`, the request triggers a JNI/FFM call to the native C/C++ implementation (`../java-exprtk-adapter` and `../c-exprtk-adapter`).
    - Otherwise, the calculation is processed by the default `com.udojava.EvalEx` engine.
  - **Response Handling:** Responses contain either the successful computation result or `NaN` in case of calculation failure.

## 3. Testing, Build, and Operations

This section details the testing framework, CI integration, and local deployment procedures.

### Testing Suite

- **`AppTestBase`:** An abstract base class providing standardized utilities and setup required for all unit and integration tests.
- **`AppTest`:** The comprehensive pure Java test harness used for validating core application logic and request routing correctness.
- **`ExprtkAdapterTest`:** Dedicated functional test suite ensuring parity and consistency between the Java-facing adapter (`../java-exprtk-adapter`) and the native implementation (`../c-exprtk-adapter`).

### Build and CI Process

- **`build.sh`:** A utility script designed to function as a lightweight Continuous Integration environment. It orchestrates validation checks for both the Java and native (JNI/FFM) components, including a full integration test driven by `curl`. _(Note: Setting `SHARED_LIBRARY_HARNESS` to `FFM` activates the FFM harness for execution.)_

### Local Launch Guide

With OpenJDK 25 in `${PATH}`, the service can be launched in two modes:

1.  **JNI Mode:**
    ```bash
    java -jar target/calc-shaded.jar
    ```
2.  **FFM Mode:**
    ```bash
    SHARED_LIBRARY_HARNESS=FFM java -jar target/calc-shaded.jar
    ```
