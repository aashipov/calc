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

run() {
    #gunicorn --chdir src app:app
    uvicorn --log-level error --host 0.0.0.0 --port 8080 --workers 4 --app-dir src app:app
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    set -e

    local DOT_VENV=.venv

    enter_venv
    run
}

closure
