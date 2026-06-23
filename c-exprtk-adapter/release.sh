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
    rm -rf ${_BUILD_DIR_NAME}
    mkdir ${_BUILD_DIR_NAME} && cd ${_BUILD_DIR_NAME}
    cmake .. -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ -DCMAKE_BUILD_TYPE=${_CMAKE_BUILD_TYPE} -DENABLE_CALCULATOR=${_ENABLE_CALCULATOR}
    make
    make install
}

test_with_calculator() {
    local _EXECUTABLE=${_SCRIPT_DIR}/${_BUILD_DIR_NAME}/exprtk-calculator/exprtk-calculator
    if [ -f ${_EXECUTABLE} ]
    then
        ${_EXECUTABLE} "2+2"
        ${_EXECUTABLE} "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2"
        ${_EXECUTABLE} "nan"
    fi
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")

    local _CMAKE_BUILD_TYPE=Release
    #local _CMAKE_BUILD_TYPE=Debug
    local _ENABLE_CALCULATOR=ON
    local _ENABLE_CALCULATOR=OFF
    local _BUILD_DIR_NAME=${_CMAKE_BUILD_TYPE}

    download_exprtk
    with_cmake
    test_with_calculator
}

closure
