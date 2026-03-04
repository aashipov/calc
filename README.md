# Algebraic Expression Evaluation Service #

## Why? ##

Ad hoc throughput framework. Compare language-specific algebraic library to native library call. 

Findings: native call is less expensive, the 'lower' the language the higher the throughput ('inconvenient convenience').

## Requirements ##

Common: bash, curl

Runtime: Docker, docker-compose

Development: OpenJDK, .NET, C++, Go, Python, Rust, Julia, Node.js

Environment configured for unprivileged installation:

```shell
LIBRARY_PATH=${HOME}/.local/lib
LD_LIBRARY_PATH=${HOME}/.local/lib
C_INCLUDE_PATH=${HOME}/.local/include:${JAVA_HOME}/include:${JAVA_HOME}/include/linux
CPLUS_INCLUDE_PATH=${HOME}/.local/include:${JAVA_HOME}/include:${JAVA_HOME}/include/linux
CMAKE_POLICY_VERSION_MINIMUM=3.5
```

## Flavors ##

Java (pure), (Tomcat, Undertow & Jetty), (Quarkus & Quarcus reactive), (Spring Boot web & webflux), (Ktor), Netty

Node.js (pure, express, nestjs), Bun (good enough for mathjs & meval, fails for exprtk native calls), Deno (stable, way higher throughput in js-ecosystem, no bindgen though)

Rust

.NET

Julia

Python

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

### Apache JMeter ###

Build local images:

```shell
./build-docker-images.sh
```

Download and extract the latest Apache JMeter distro, copy `./test` catalog to JMeter dir, run harness `./test/composed-runner.sh`

[Result on a consumer-grade PC](https://github.com/aashipov/openjdk-build/releases/download/1.0.0/i5-calc-load-test.zip)

## License

Perl The "Artistic License"
