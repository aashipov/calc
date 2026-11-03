# Deno Oak Flavor

[c-exprtk-adapter](../c-exprtk-adapter) and [c-meval-adapter](../c-meval-adapter) RESTFul adapter in Deno Oak.

## Requirements

[Deno](https://github.com/denoland/deno/releases/latest/download/deno-x86_64-unknown-linux-gnu.zip) and Rust in `${PATH}`

Build `../c-exprtk-adapter` and `../c-meval-adapter` first

## Build

```sh
deno install
deno run build
```

### Wrapper

```sh
./build.sh
```

## Run

```sh
deno serve --allow-all calc.ts
```

### Platform-specific executable

```sh
./calc --parallel
```
