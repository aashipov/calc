# Bun Flavor

[c-exprtk-adapter](../c-exprtk-adapter) and [c-meval-adapter](../c-meval-adapter) RESTFul adapter in pure Bun.

## Requirements

[Bun](https://bun.com), Rust in `${PATH}`

Build `../c-exprtk-adapter` and `../c-meval-adapter` first

## Build

### Console to executable

```sh
bun install && bun build calc.ts --compile
```

### Console to executable wrapper with tests

```sh
./build.sh
```

### Make

```sh
make clean && make
```

## Run

### Executable

```sh
./calc
```

### bun

```sh
bun calc.ts
```

```sh
bun calc-cluster.ts
```

### Make

```sh
make run
```

```sh
make run-cluster
```
