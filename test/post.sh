#!/bin/sh

do_get() {
    printf "\n"
    printf "GET ${1}\n"
    ${CURL_CMD} ${1}
    printf "\n"
}

do_post() {
    printf "\n"
    printf "POST ${1} with ${2} \n"
    ${CURL_CMD} -X POST -H "Content-Type: text/plain" -d ${2} ${1}
    printf "\n"
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    #set -x

    local BASE_URL="http://0.0.0.0:8080"
    local COMPLEX_EXPRESSION=`cat sample.expression`
    local CURL_CMD="curl"
    #local CURL_CMD="curl -v"

    do_get ${BASE_URL}

    do_post ${BASE_URL} ${COMPLEX_EXPRESSION}
    do_post ${BASE_URL}/mxparser ${COMPLEX_EXPRESSION}
    do_post ${BASE_URL}/exprtk ${COMPLEX_EXPRESSION}
}

closure
