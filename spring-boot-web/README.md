# Spring Boot flavor

## Native Build

```shell
mvn native:compile -Dnative -DskipTests
```

## Run

```shell
java -Dserver.tomcat.threads.max=`getconf _NPROCESSORS_ONLN` -jar target/calc-shaded.jar
```

```shell
java -Djdk.virtualThreadScheduler.parallelism=`getconf _NPROCESSORS_ONLN` -Dspring.threads.virtual.enabled=true -jar target/calc-shaded.jar
```

## Swagger UI

[OpenAPI UI](http://localhost:8080/openapi-ui) (a few flavors)
