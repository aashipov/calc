#!/bin/sh

download_httplib() {
    if [ ! -f ${HOME}/.local/include/httplib.h ]
    then
        mkdir -p ${HOME}/.local/include/
        curl -L https://raw.githubusercontent.com/yhirose/cpp-httplib/refs/heads/master/httplib.h -o ${HOME}/.local/include/httplib.h
    fi
}

with_cmake() {
    cd ${_SCRIPT_DIR}
    rm -rf ${_BUILD_DIR_NAME}
    mkdir ${_BUILD_DIR_NAME} && cd ${_BUILD_DIR_NAME}
    cmake .. -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ -DCMAKE_BUILD_TYPE=${_CMAKE_BUILD_TYPE} -DENABLE_TESTS=ON
    make
    #make install
}

test_calc() {
    cd ${_SCRIPT_DIR}/${_BUILD_DIR_NAME}
    ctest --test-dir ./tests/
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")

    local _CMAKE_BUILD_TYPE=Release
    #local _CMAKE_BUILD_TYPE=Debug
    local _BUILD_DIR_NAME=${_CMAKE_BUILD_TYPE}

    download_httplib
    with_cmake
    test_calc
}

closure
