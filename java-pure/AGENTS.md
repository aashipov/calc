# Pure Java Calculator Service: Implementation Architecture

This document provides a detailed technical overview of the pure Java algebraic computation service. This service is designed to accept algebraic expressions via HTTP and return the computed results by routing requests to specialized, high-performance calculation engines.

## Core Technologies and Dependencies

The service is built upon a diverse stack of components to manage HTTP communication, dependency resolution, and robust, high-performance computation.

- **HTTP Server:** Leverages `com.sun.net.httpserver.HttpServer` for lightweight and native HTTP request handling.
- **Computation Engines:** The service utilizes a polyglot computation strategy, routing calculations based on URI context:
  - **`exprtk` (Native/High-Performance):** Accessed via JNI/FFM through the `../java-exprtk-adapter` (and its native counterpart, `../c-exprtk-adapter`). This engine is used for optimal speed and complex algebraic evaluation, provides an estimate of java-mediated native call overhead
  - **`org-mXparser` (Specialized/Intensive):** Used when the URI specifies `mxparser`. This engine is reserved for highly specialized or complex calculation sets and is noted for its significant resource consumption.
  - **`com.udojava.EvalEx` (Fallback/General Purpose):** Serves as a reliable general-purpose evaluator and the default fallback mechanism when no specialized engine is requested.

## Architectural Design and Components

Maven manages dependency resolution and the overall build lifecycle. The core logic is structured within the `org.dummy.calc` package:

### Application Layer (`org.dummy.calc`)

- **`App` (Entry Point):** This class initializes and encapsulates the `com.sun.net.httpserver.HttpServer` instance, serving as the primary bootstrapper for the entire service.
- **`CalcHandler` (Request Router):** Implements `com.sun.net.httpserver.HttpHandler` and functions as the central request router, managing all incoming GET and POST requests:
  - **GET Requests:** Always responds with a defined `WELCOME` status, serving as an API health check.
  - **POST Requests (Routing Logic):**
    - If the URI contains `mxparser`, calculation is routed to the `org-mXparser` engine.
    - If the URI specifies `exprtk`, the request triggers a JNI/FFM call to the native C/C++ implementation (`../java-exprtk-adapter` and `../c-exprtk-adapter`).
    - Otherwise, the calculation is processed by `com.udojava.EvalEx`.
    - Responses will contain either the computation result or a specific `NAN` status indicating an error.

### Testing and Deployment

- **Test Harness:**
  - **`AppTestBase`:** An abstract base class providing standardized utilities and setup for all unit and integration tests.
  - **`AppTest`:** The comprehensive pure Java test harness used for validating application logic and routing correctness.
  - **`ExprtkAdapterTest`:** Ensures functional parity and consistency between the Java-facing adapter (`../java-exprtk-adapter`) and the native implementation (`../c-exprtk-adapter`).
- **Build and CI (`build.sh`):** A utility script designed to function as a lightweight CI environment. It orchestrates validation checks for both the Java and native (JNI/FFM) components, including an integration test driven by `curl`. _(Note: Setting `SHARED_LIBRARY_HARNESS` to `FFM` activates the FFM harness for execution.)_
- **Launch:** with openJDK 25 in `${PATH}`, execute `java -jar target/calc-shaded.jar` or `SHARED_LIBRARY_HARNESS=FFM java -jar target/calc-shaded.jar` in console
