# Quarkus flavor

## Run

```shell
java -Dquarkus.thread-pool.max-threads=`getconf _NPROCESSORS_ONLN` -jar target/calc-shaded.jar
```
