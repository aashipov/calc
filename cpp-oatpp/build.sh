#!/bin/sh

with_cmake() {
    cd ${_SCRIPT_DIR}
    rm -rf ${BUILD_DIR_NAME}
    mkdir ${BUILD_DIR_NAME} && cd ${BUILD_DIR_NAME}
    cmake .. -DCMAKE_INSTALL_PREFIX=${HOME}/.local/ -DENABLE_TESTS=ON
    make
    make install
}

integration_test() {
    cd ${_SCRIPT_DIR}/${BUILD_DIR_NAME}

    cp ${EXECUTABLE_NAME_ORIG} ${EXECUTABLE_NAME}
    ./${EXECUTABLE_NAME} &
    sleep 5s

    ${_SCRIPT_DIR}/../test/calc-test.sh
    local TEST_STATUS=${?}

    pkill -f ${EXECUTABLE_NAME}

    if [ ${TEST_STATUS} -ne 0 ]
    then
        printf "Test failed\n"
        exit ${TEST_STATUS}
    fi
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")

    # Comment following line out for debug
    export CMAKE_BUILD_TYPE=Release
    local BUILD_DIR_NAME="build"
    local EXECUTABLE_NAME_ORIG="calc"
    local EXECUTABLE_NAME="calc-it"

    with_cmake
    integration_test
}

closure
