#!/bin/sh

do_get(){
    printf "\nGET: "
    local RESULT=`curl -s ${BASE_URL}`
    case "$RESULT" in
        *Welcome*)
            printf "OK"
        ;;
        *)
            printf "GET failed: ${RESULT}"
            exit 1
        ;;
    esac
}

do_post(){
    local ENDPOINT="${1}"
    printf "\nPOST ${ENDPOINT}: "
    local RESULT=`curl -s -X POST -H "Content-Type: text/plain" --data-binary @"${SAMPLE_EXPRESSION}" ${BASE_URL}/${ENDPOINT}`
    case "$RESULT" in
        *19.9884*)
            printf "OK"
        ;;
        *)
            printf "POST ${ENDPOINT} failed: ${RESULT}"
            exit 1
        ;;
    esac
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    set -e

    local SAMPLE_EXPRESSION="sample.expression"
    local BASE_URL="http://0.0.0.0:8080"
    
    printf "***"
    do_get
    do_post
    do_post "mxparser"
    do_post "exprtk"
    printf "\n***\n"
}

closure
