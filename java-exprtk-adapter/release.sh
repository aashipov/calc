#!/bin/sh

build_header() {
    cd ${_SCRIPT_DIR}
    javac -h . "${_SCRIPT_DIR}/src/main/java/org/dummy/calc/JavaExprtkAdapter.java"
    rm "${_SCRIPT_DIR}/src/main/java/org/dummy/calc/JavaExprtkAdapter.class"
}

build_native_library() {
    cd ${_SCRIPT_DIR}

    local BUILD_DIR_NAME=${_CMAKE_BUILD_TYPE}
    rm -rf ${BUILD_DIR_NAME}
    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}

    cmake -DCMAKE_BUILD_TYPE=${_CMAKE_BUILD_TYPE} -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ ..
    make install
}

maven_build() {
    cd ${_SCRIPT_DIR}
    mvn clean package -DskipTests
    mvn test
    SHARED_LIBRARY_HARNESS=FFM mvn test
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    set -e

    local _CMAKE_BUILD_TYPE=Release

    build_header
    build_native_library
    maven_build
}

closure
