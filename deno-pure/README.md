# Deno Flavor

## Requirements

Build `../c-exprtk-adapter` first

[Deno](https://github.com/denoland/deno/releases) and Rust in `${PATH}`

## Build & run

```shell
deno install && deno run wasmbuild
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
deno run build && ./calc
```

## References ##

[wasmbuild](https://github.com/denoland/wasmbuild)
