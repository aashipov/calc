# Ruby Async::HTTP flavor

## Requirements

```shell
sudo pacman -S ruby
```

## Build

```shell
gem install -g
```

Symlink falcon from `${HOME}/.local/share/gem/ruby/3.4.0/bin/` to `${PATH}`

## Run

```shell
falcon serve --bind http://0.0.0.0:8080
```
