#!/bin/sh

# git clone https://github.com/oatpp/oatpp.git && cd oatpp/

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    
    git fetch --all --tags
    
    local TAG_TO_BUILD="1.3.0"
    git checkout tags/${TAG_TO_BUILD}
    
    local BUILD_DIR_NAME="build"
    rm -rf ${BUILD_DIR_NAME}
    
    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}
    
    cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ ..
    make -j4
    make install
}

closure
