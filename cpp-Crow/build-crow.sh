#!/bin/sh
# git clone https://github.com/drogonframework/drogon.git
# git submodule update --init
closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    git fetch --all --tags

    local TAG_TO_BUILD="v1.3.0"
    git checkout tags/${TAG_TO_BUILD}

    local BUILD_DIR_NAME="build"
    rm -rf ${BUILD_DIR_NAME}

    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}

    cmake -DCROW_BUILD_EXAMPLES=OFF -DCROW_BUILD_TESTS=OFF -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ ..
    make install
}

closure
