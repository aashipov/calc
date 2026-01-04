# Algebraic Expression Evaluation Service

## Requirements

Common: bash, curl

Runtime: Docker, docker-compose

Development: OpenJDK 17+

## Flavors

Pure java 

Tomcat, Undertow & Jetty

Quarkus & Quarcus reactive

Spring Boot web & webflux

Ktor

Node.js (pure, express, nestjs)

Rust

.NET

Julia

## How to use

```shell
curl -X POST -H "Content-Type: text/plain" --data "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2" http://localhost:8080
printf "\n"
curl -X POST -H "Content-Type: text/plain" --data "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2" http://localhost:8080/mxparser
printf "\n"
```

prints

```shell
19.9884
19.98843289048526
```

OpenAPI UI is available with several flavors at ```http://localhost:8080/openapi-ui```

## Load test ##

Check `test` directory

[Result on a consumer-grade PC](https://github.com/aashipov/openjdk-build/releases/download/1.0.0/i5-calc-load-test.zip)

## License

Perl The "Artistic License"
