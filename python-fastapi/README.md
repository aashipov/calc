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

## Remote Debug

```shell
./debug.sh
```

In Zed hit F4 and pick `Remote Debug`

## Profile & visualize

```shell
./profile.sh
```

Apply the load, e.g. `../test/calc-wrk.sh`

Stop the `profile.sh` app, call `snakewizw.sh`, explore results in browser

## Performance

`gunicorn` above worker per CPU core won't increase throughput on a consumer grade PC

## API

[Swagger](http://0.0.0.0:8080/openapi-ui)
