#!/bin/sh
# git clone https://github.com/CrowCpp/Crow.git

clone_framework() {
    if [ ! -d ${_SCRIPT_DIR}/${FRAMEWORK_NAME} ]
    then
        cd ${_SCRIPT_DIR}/
        git clone ${CLONE_URL}
    fi
}

build_framework() {
    cd ${_SCRIPT_DIR}/${FRAMEWORK_NAME}
    git fetch --all --tags
    git checkout tags/${TAG_TO_BUILD}

    rm -rf ${BUILD_DIR_NAME}

    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}

    cmake -DCROW_BUILD_EXAMPLES=OFF -DCROW_BUILD_TESTS=OFF -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ ..
    make install
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    
    local BUILD_DIR_NAME="build"
    local FRAMEWORK_NAME=Crow
    local CLONE_URL=https://github.com/CrowCpp/Crow.git
    local TAG_TO_BUILD="v1.3.1"

    clone_framework
    build_framework
}

closure
