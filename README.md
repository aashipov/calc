# Algebraic Expression Evaluation Service

## Requirements

Common: bash, curl

Runtime: Docker

Development: OpenJDK 17+

## Flavors

Pure java

Helidon SE & MP

Quarkus & Quarcus reactive

Spring Boot web & webflux

Rust


## How to use

```curl -X POST -H "Content-Type: text/plain" --data "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2" http://localhost:8080``` prints 19.9884

```curl -X POST -H "Content-Type: text/plain" --data "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2" http://localhost:8080/mxparser``` prints 19.98843289048526

OpenAPI UI ```http://localhost:8080/openapi-ui```

## License

Perl The "Artistic License"
