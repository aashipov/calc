# Algebraic Expression Evaluation Service #

## Requirements ##

Common: bash, curl

Runtime: Docker, docker-compose

Development: OpenJDK 17+, .NET, Rust, Python

## Flavors ##

Java (pure), (Tomcat, Undertow & Jetty), (Quarkus & Quarcus reactive), (Spring Boot web & webflux), (Ktor)

Node.js (pure, express, nestjs)

Rust

.NET

Julia

Python

## Showcase ##

```shell
./test/post.bash
```

[OpenAPI UI](http://localhost:8080/openapi-ui) (a few flavors)

## Load test ##

### Apache Benchmark ###

```shell
./test/benchmark.sh
```

### Apache JMeter ###

Harness `./test/composed-runner.sh`

[Result on a consumer-grade PC](https://github.com/aashipov/openjdk-build/releases/download/1.0.0/i5-calc-load-test.zip)

## License

Perl The "Artistic License"
