#!/bin/sh

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    local SAMPLE_EXPRESSION="sample.expression"

    local BASE_URL="http://0.0.0.0:8080"
    printf "health\n"
    ab -n 100000 -c 1000 ${BASE_URL}/health
    printf "evalex\n"
    ab -n 100000 -c 1000 -p ${SAMPLE_EXPRESSION} ${BASE_URL}/
    printf "mxparser\n"
    ab -n 100000 -c 1000 -p ${SAMPLE_EXPRESSION} ${BASE_URL}/mxparser
    printf "exprtk\n"
    ab -n 100000 -c 1000 -p ${SAMPLE_EXPRESSION} ${BASE_URL}/exprtk
    printf "\n"
}

closure
