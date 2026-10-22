# Calculator Service: Helidon SE flavor

Build `../java-expr-adapter` first

## Build & run via make

### JNI

```sh
make && make run
```

### FFM

```sh
make && make run SHARED_LIBRARY_HARNESS=FFM
```

## Build & run with console

### JNI

```sh
mvn clean package
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -jar target/calc-shaded.jar
```

### FFM

```sh
mvn clean package
SHARED_LIBRARY_HARNESS=FFM java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005 -jar target/calc-shaded.jar
```
