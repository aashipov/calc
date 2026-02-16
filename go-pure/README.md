# Go flavor #

[C++ Mathematical Expression Parsing And Evaluation Library](https://github.com/ArashPartow/exprtk) RESTFul adapter in Go

## Requirements ##

`go`, [dlv](https://github.com/go-delve/delve) in `${PATH}`

Build `../c-exprtk-adapter` first

## Build ##

### Debug ###

```shell
go build -gcflags "all=-N -l" -o app-debug
```

```shell
dlv exec --listen=:2345 --headless --api-version=2 --accept-multiclient --continue ./app-debug
```

Use remote debug

### Release ###

```shell
go build -o app
```

## Run ##

`app` or `app-debug`
