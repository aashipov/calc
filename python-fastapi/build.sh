#!/bin/sh

enter_venv() {
    if [ ! -d ${_SCRIPT_DIR}/${DOT_VENV} ]
    then
        python3 -m venv ${_SCRIPT_DIR}/${DOT_VENV}
        . ${_SCRIPT_DIR}/${DOT_VENV}/bin/activate
        pip3 install -e .
    else
        . ${_SCRIPT_DIR}/${DOT_VENV}/bin/activate
    fi
}

test() {
    if [ -d ${_SCRIPT_DIR}/tests ]
    then
        python3 -m pytest
    fi
}

distro() {
    python3 -m build --wheel
    local WHEEL_FILE=./dist/$(ls dist | grep whl)
    python3 -m pip install --force-reinstall ${WHEEL_FILE}
    python3 -m pip uninstall -y ${WHEEL_FILE}
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    set -e

    local DOT_VENV=.venv

    enter_venv
    test
    distro
}

closure
