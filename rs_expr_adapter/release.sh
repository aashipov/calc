#!/bin/sh

build() {
    cargo test
    cargo build --release
}

deploy() {
    # h
    if [ -f ${INCLUDE_PATH_PREFIX}/${HEADER_NAME} ]
    then
        rm ${INCLUDE_PATH_PREFIX}/${HEADER_NAME}
    fi

    if [ -f ${RS_LIBRARY_DIR}/${HEADER_NAME} ]
    then
        cp ${RS_LIBRARY_DIR}/${HEADER_NAME} ${INCLUDE_PATH_PREFIX}
    fi
    # so
    if [ -f ${LIBRARY_PATH_PREFIX}/${LIBRARY_NAME} ]
    then
        rm ${LIBRARY_PATH_PREFIX}/${LIBRARY_NAME}
    fi

    if [ -f ${RS_LIBRARY_DIR}/target/release/${LIBRARY_NAME} ]
    then
        cp ${RS_LIBRARY_DIR}/target/release/${LIBRARY_NAME} ${LIBRARY_PATH_PREFIX}
    fi
}

closure() {
    # https://stackoverflow.com/a/1482133
    # Consistent across Linux bash, Cygwin terminal and Git Bash
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    local RS_LIBRARY_DIR=${_SCRIPT_DIR}
    cd ${RS_LIBRARY_DIR}

    local LIBRARY_PATH_PREFIX=${HOME}/.local/lib
    local LIBRARY_NAME="librs_expr_adapter"
    local INCLUDE_PATH_PREFIX=${HOME}/.local/include
    local HEADER_NAME="rs-expr-adapter.h"

    case ${OSTYPE} in
        *linux*)
            LIBRARY_NAME="${LIBRARY_NAME}.so"
            ;;
        *)
            printf "${OSTYPE} is not supported"
            exit 1
            ;;
    esac

    build
    deploy
}

closure
