# Jetty flavor

## Run

```shell
java -Djdk.virtualThreadScheduler.parallelism=`getconf _NPROCESSORS_ONLN` -jar target/calc-shaded.jar
```
