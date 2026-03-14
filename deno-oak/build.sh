#!/bin/sh

# 'Reusable integration test' for 'not-yet-industrial-grade' languages

build() {
    cd ${_SCRIPT_DIR}
    deno install
    deno run build
    cp ${EXECUTABLE_NAME_ORIG} ${EXECUTABLE_NAME}
}

integration_test() {
    cd ${_SCRIPT_DIR}
    
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
    cd ${_SCRIPT_DIR}
    
    local EXECUTABLE_NAME_ORIG="calc"
    local EXECUTABLE_NAME="calc-it"
    
    build
    integration_test
}

closure
