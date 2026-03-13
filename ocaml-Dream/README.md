# OCaml Dune flavor #

## Requirements

Build `../c-exprtk-adapter` first

`opam` in `${PATH}`

```shell
opam install ctypes ctypes-foreign dream
```

```shell
dune build
```

```shell
./_build/default/calc.exe
```

## Limitations ##

As slow as deno
