#!/bin/sh

# 'Reusable integration test' for 'not-yet-industrial-grade' languages

build() {
    cd ${_SCRIPT_DIR}
    dune build
}

integration_test() {
    cd ${_SCRIPT_DIR}
    
    ./_build/default/${EXECUTABLE_NAME} &
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
    cd ${_SCRIPT_DIR}
    
    local EXECUTABLE_NAME="calc.exe"
    
    build
    integration_test
}

closure
