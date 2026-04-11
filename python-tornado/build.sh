#!/bin/sh

enter_venv() {
    if [ ! -d ${_SCRIPT_DIR}/${DOT_VENV} ]
    then
        mkdir -p ${_SCRIPT_DIR}/${DOT_VENV}
        python3 -m venv ${_SCRIPT_DIR}/${DOT_VENV}
        . ${_SCRIPT_DIR}/${DOT_VENV}/bin/activate
        pip3 install hatch==1.16.3 build==1.4.0
    else
        . ${_SCRIPT_DIR}/${DOT_VENV}/bin/activate
    fi
}

build() {
    pip3 install .
}

test() {
    pytest
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

distro() {
    python3 -m build --wheel
    pip3 install ./dist/$(ls dist | grep whl)
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    set -e

    local DOT_VENV=.venv
    local EXECUTABLE_NAME="run.sh"

    enter_venv
    build
    test
    integration_test
    distro
}

closure
