# Go FastHTTP flavor

[c-exprtk-adapter](../c-exprtk-adapter) and [c-meval-adapter](../c-meval-adapter) RESTFul adapter in Go FastHTTP

## Requirements

`go`, `gcc-go`, [swag](https://github.com/swaggo/swag), [dlv](https://github.com/go-delve/delve) in `${PATH}`

```sh
go install github.com/go-delve/delve/cmd/dlv@latest && go install github.com/swaggo/swag/cmd/swag@latest
```

Build `../c-exprtk-adapter` and `../c-meval-adapter` first

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
