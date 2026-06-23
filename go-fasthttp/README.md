# Go FastHTTP flavor

[C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter in Go

## Requirements

`go`, `gcc-go`, [swag](https://github.com/swaggo/swag), [dlv](https://github.com/go-delve/delve) in `${PATH}`

Build `../c-exprtk-adapter` first

## Build

Call `make` to test & build everything or follow paragraphs below

### Debug

```shell
swag init --output ./swagger && go build -gcflags "all=-N -l" -o app-debug && go test
```

```shell
dlv exec --listen=:2345 --headless --api-version=2 --accept-multiclient --continue ./app-debug
```

Use remote debug

### Release

```shell
swag init --output ./swagger && go build -o app && go test
```

## Run

`app` or `app-debug`

## Swagger UI

[SwaggerUI](http://0.0.0.0:8080/openapi-ui/index.html)
