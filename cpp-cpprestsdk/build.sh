#!/bin/sh

with_cmake() {
    cd ${_SCRIPT_DIR}
    rm -rf ${BUILD_DIR_NAME}
    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}
    cmake .. -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ -DENABLE_TESTS=ON
    make
    make install
}

test_calc() {
    cd ${_SCRIPT_DIR}/${BUILD_DIR_NAME}
    ctest --test-dir ./tests/
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    
    # Comment following line out for debug
    export CMAKE_BUILD_TYPE=Release
    local BUILD_DIR_NAME="build"
    
    with_cmake
    test_calc
}

closure
