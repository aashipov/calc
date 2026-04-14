# Algebraic Expression Evaluation Service #

## Why? ##

Ad hoc throughput framework. Compare language-specific algebraic library to native library call. 

Findings: native call is less expensive, the 'lower' the language the higher the throughput ('inconvenient convenience'), dockerized (bridged net) is twice as slow as baremetal at least.

## Requirements ##

Common: bash, curl

Runtime: Docker, docker-compose

Development: OpenJDK, .NET, C++, Go, Python, Rust, Julia, Node.js, D, Dart, OCaml

Environment configured for unprivileged installation:

```shell
LIBRARY_PATH=${HOME}/.local/lib
LD_LIBRARY_PATH=${HOME}/.local/lib
C_INCLUDE_PATH=${HOME}/.local/include:${JAVA_HOME}/include:${JAVA_HOME}/include/linux
CPLUS_INCLUDE_PATH=${HOME}/.local/include:${JAVA_HOME}/include:${JAVA_HOME}/include/linux
CMAKE_POLICY_VERSION_MINIMUM=3.5
```

## Flavors ##

C++ (cpprestsdk, Crow, Drogon, httplib, oatpp, POCO).

Java (pure), (Tomcat, Undertow, Jetty, Netty, Vert.x), (Quarkus & Quarcus reactive), (Spring Boot web & webflux), (Ktor), (Helidon SE, Helidon PE), Micronaut, (Scala 3 ZIO HTTP & Cask)

Node.js (pure, express, nestjs), Bun, Deno

Rust

.NET

Julia

Python

OCaml

D (serverino, Vibe.d)

Dart

## Showcase for any flavor up & running ##

```shell
./test/calc-test.bash
```

[OpenAPI UI](http://localhost:8080/openapi-ui) (a few flavors)

## Load test ##

### Apache Benchmark ###

```shell
./test/calc-benchmark.sh
```

### wrk ###

```shell
./test/calc-wrk.sh
```

### Apache JMeter Baremetal ###

Build all the implementations, copy `test` dir to jmeter dir (same parent dir as this repo), the call:

```shell
./baremetal-load-runner.sh
```

### Apache JMeter Dockerized ###

Build local images:

```shell
./build/build-docker-images.sh
```

Download and extract the latest Apache JMeter distro, copy `./test` catalog to JMeter dir, run harness `./test/composed-runner.sh`

### Showcase

[Result on a consumer-grade PC](https://github.com/aashipov/openjdk-build/releases/download/1.0.0/i5-calc-load-test.zip)

'Slow by design' ecosystems (like python or ECMAScript) baremetal throughput in cluster form (bun/Node.js built-in or gunicorn) is a bit faster than haproxy with multiple copies on different ports [results](https://github.com/aashipov/openjdk-build/releases/download/1.0.0/i5-calc-load-test-baremetal-haproxied.zip)

## License

Perl The "Artistic License"
