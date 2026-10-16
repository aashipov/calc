# Go pure flavor #

[c-exprtk-adapter](../c-exprtk-adapter) and [c-meval-adapter](../c-meval-adapter) RESTFul adapter in pure Go.

## Requirements ##

`go`, `gcc-go`, [dlv](https://github.com/go-delve/delve) in `${PATH}`

Build `../c-exprtk-adapter` and `../c-meval-adapter` first

## Build ##

Call `make` to test & build everything or follow paragraphs below

### Debug ###

```shell
go build -gcflags "all=-N -l" -o app-debug && go test
```

```shell
dlv exec --listen=:2345 --headless --api-version=2 --accept-multiclient --continue ./app-debug
```

Use remote debug

### Release ###

```shell
go build -o app && go test
```

## Run ##

`app` or `app-debug`
