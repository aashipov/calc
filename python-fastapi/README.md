# Python FastAPI flavor

## Requirements

Build `../c-exprtk-adapter` first

## Build

```shell
./build.sh
```

## Run

```shell
./run.sh
```

## Performance

`gunicorn` workers count above 8 won't increase throughput on a consumer grade PC

[Swagger](http://0.0.0.0:8080/openapi-ui)
