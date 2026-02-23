#!/bin/sh

download_exprtk() {
    if [ ! -f ${HOME}/.local/include/exprtk.hpp ]
    then
        mkdir -p ${HOME}/.local/include/
        curl -L https://raw.githubusercontent.com/ArashPartow/exprtk/refs/heads/master/exprtk.hpp -o ${HOME}/.local/include/exprtk.hpp
    fi
}

with_cmake() {
    cd ${_SCRIPT_DIR}
    rm -rf ${BUILD_DIR_NAME}
    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}
    cmake .. -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ -DCMAKE_BUILD_TYPE=Release
    make
    make install
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")

    local BUILD_DIR_NAME="build"
    
    download_exprtk
    with_cmake
}

closure
