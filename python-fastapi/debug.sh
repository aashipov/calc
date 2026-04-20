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

debug() {
    python3 -m debugpy --listen 0.0.0.0:5678 src/app.py
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    set -e

    local DOT_VENV=.venv

    enter_venv
    debug
}

closure
