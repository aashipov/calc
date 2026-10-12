# Rust meval C ABI adapter

Rust [meval-rs](https://github.com/rekka/meval-rs) math expression parsing & evaluation C ABI adapter

## Build

### Debug

```sh
make PROFILE=debug
```

### Release

```sh
make
```

## (un)install

### user

```sh
make && make install PREFIX=${HOME}/.local
```

### system

```sh
make && sudo make install
```
