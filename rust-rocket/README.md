# Rocket Flavor

## Requirements

Build `../c-exprtk-adapter` and `../c-meval-adapter` first

## Build & run

### Debug

```sh
cargo build && cargo test && ./target/debug/calc
```

or

```sh
make PROFILE=debug && ./target/debug/calc
```

### Release

```sh
cargo build --release && cargo test --release && ./target/release/calc
```

or

```sh
make && ./target/release/calc
```

[SwaggerUI](http://0.0.0.0:8080/openapi-ui/)
