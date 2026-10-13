# C exprtk adapter

Download exprtk the lib to user's include directory:

```shell
curl -L https://raw.githubusercontent.com/ArashPartow/exprtk/refs/heads/master/exprtk.hpp -o ${HOME}/.local/include/exprtk.hpp
```

## Build with script

### Debug

```sh
./debug.sh
```

### Release

```sh
./release.sh
```

## Build with make

### user

```sh
make clean && make PREFIX=${HOME}/.local && make install PREFIX=${HOME}/.local
```

### system

```sh
make clean && sudo make && sudo make install
```
