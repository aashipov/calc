# C exprtk adapter #

Download exprtk the lib to user's include directory:

```shell
curl -L https://raw.githubusercontent.com/ArashPartow/exprtk/refs/heads/master/exprtk.hpp -o ${HOME}/.local/include/exprtk.hpp
```

```shell
rm -rf build && mkdir -p build && cd build && cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ .. && make install
```
