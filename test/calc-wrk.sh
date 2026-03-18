#!/bin/sh

# Install https://github.com/wg/wrk

do_get(){
    printf "***\nGET\n"
    ${WRK_CMD} ${BASE_URL}/
    printf "\n***\n\n\n"
}

do_post(){
    local ENDPOINT="${1}"
    printf "***\nPOST ${ENDPOINT}\n"
    ${WRK_CMD} -s sample.expression.lua ${BASE_URL}/${ENDPOINT}
    printf "\n***\n\n\n"
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    set -e

    local SAMPLE_EXPRESSION="sample.expression"
    local BASE_URL="http://0.0.0.0:8080"
    local WRK_CMD="wrk -t8 -c1000 -d30s"

    do_get
    do_post
    do_post "mxparser"
    do_post "exprtk"
}

closure
