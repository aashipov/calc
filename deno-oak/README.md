# Deno Oak Flavor

## Requirements

Build `../c-exprtk-adapter` first

[Deno](https://github.com/denoland/deno/releases) and Rust in `${PATH}`

## Build & run

```shell
deno install
```

```shell
deno serve --parallel --allow-all calc.ts
```

```shell
deno run dev
```

```shell
deno run start
```

Platform-specific executable:

```shell
deno run build && ./calc --parallel
```
