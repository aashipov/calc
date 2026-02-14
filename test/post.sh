#!/bin/sh

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    
    local SAMPLE_EXPRESSION="sample.expression"
    
    local BASE_URL="http://0.0.0.0:8080"
    curl ${BASE_URL}
    printf "\n"
    curl -X POST -H "Content-Type: text/plain" --data-binary @"${SAMPLE_EXPRESSION}" ${BASE_URL}
    printf "\n"
    curl -X POST -H "Content-Type: text/plain" --data-binary @"${SAMPLE_EXPRESSION}" ${BASE_URL}/mxparser
    printf "\n"
    curl -X POST -H "Content-Type: text/plain" --data-binary @"${SAMPLE_EXPRESSION}" ${BASE_URL}/exprtk
    printf "\n"
}

closure
