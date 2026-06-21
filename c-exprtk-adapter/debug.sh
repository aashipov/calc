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
    cmake .. -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ -DCMAKE_BUILD_TYPE=Debug -DENABLE_CALCULATOR=ON
    make
    make install
}

test_with_calculator() {
    local EXECUTABLE=${_SCRIPT_DIR}/${BUILD_DIR_NAME}/exprtk-calculator/exprtk-calculator
    ${EXECUTABLE} "2+2"
    ${EXECUTABLE} "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
    ${EXECUTABLE} "nan"
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")

    local BUILD_DIR_NAME="build"

    download_exprtk
    with_cmake
    test_with_calculator
}

closure
