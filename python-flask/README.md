# Python Flask flavor

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
