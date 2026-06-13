# Pure Java Calculator Service: Implementation Overview

This pure Java algebraic computation service is designed to handle incoming algebraic expressions via HTTP and return the computed results.

## Dependencies and Core Technologies

The service relies on several external libraries for robust computation and communication:

- **HTTP Server:** Uses `com.sun.net.httpserver.HttpServer` for request handling.
- **Computation Engines:**
  - `../java-exprtk-adapter` (and its C/C++ counterpart, `../c-exprtk-adapter`): Utilizes the `exprtk` library, accessed via JNI/FFM for high-performance native calculation.
  - `org.mariuszgromada.math.MathParser.org-mXparser`: Used for specific, complex calculations when the URI suggests its use (Note: This engine is noted for being resource-intensive).
  - `com.udojava.EvalEx`: Serves as a fallback or general-purpose evaluation engine.

## Project Structure

Maven is utilized for stable dependency management and build orchestration. The core components are organized as follows:

### Application Layer (`org.dummy.calc`)

- **`App`:** The entry point. Initializes and encapsulates the `com.sun.net.httpserver.HttpServer` instance, allowing the service to begin accepting HTTP requests.
- **`CalcHandler`:** Implements `com.sun.net.httpserver.HttpHandler` to manage all incoming GET and POST requests.
  - **GET Requests:** Always returns a `WELCOME` status/message.
  - **POST Requests:**
    - If the URI contains `mxparser`, the calculation is handled by `org-mXparser`.
    - If the URI specifies `exprtk`, the request triggers a JNI/FFM call to the native C library (`../c-exprtk-adapter`).
    - Otherwise, the calculation falls back to `com.udojava.EvalEx`.
    - The response will contain either a computation result or a `NAN` status.
- **`JavaExprtkAdapter`:** A shim layer that links the Java code to the native implementation (`../java-exprtk-adapter` symlinks to the JNI/FFM harness for `libc-exprtk-adapter`).

### Testing and Build System

- **`AppTestBase`:** A minimalistic, abstract base class used for standardized test coverage.
- **`AppTest`:** The pure Java test harness for functional validation.
- **`ExprtkAdapterTest`:** `../java-exprtk-adapter`, hence `../c-exprtk-adapter` consistency check
- **`build.sh`:** A utility script that acts as a lightweight CI environment. It validates both the Java and native (JNI/FFM) components, and includes an additional curl-driven integration test (Note: `SHARED_LIBRARY_HARNESS` environment variable set to `FFM` enables FFM harness).
