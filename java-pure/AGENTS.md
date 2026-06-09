# Calculator service: Pure java flavor

Relies upon `../c-exprtk-adapter` and `../java-exprtk-adapter` to perform algebraic computation

## Project Structure

Well-seasonned maven as dependency & build tool

`org.dummy.calc.App` encompasses `com.sun.net.httpserver.HttpServer` to handle incoming requests

`org.dummy.calc.JavaExprtkAdapter` symlinks `../java-exprtk-adapter`, JNI/FFM harness to `libc-exprtk-adapter` (see `../c-exprtk-adapter`). Well-seasonned JNI

`org.dummy.calc.CalcHandler` implements `com.sun.net.httpserver.HttpHandler` for GET/POST. Mere regex against request URL is preferred over sophisticated strategies

`org.dummy.calc.AppTestBase` - an JVM-specific abstract minimalistic cut-n-paste test coverage, `org.dummy.calc.AppTest` - pure java test harness

`build.sh` drop-in replacement for a full-fledged CI, checks both JNI & FFM, an extra step - curl-driven integration test
