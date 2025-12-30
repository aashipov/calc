#!/bin/sh

threads_count() {
    local MAX_THREADS=8
    local CPU_COUNT=$(($(getconf _NPROCESSORS_ONLN) + 0))
    if ((${CPU_COUNT} > ${MAX_THREADS})); then
        echo ${MAX_THREADS}
    else
        echo ${CPU_COUNT}
    fi
}

closure() {
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    julia --project=. --threads=$(threads_count) --heap-size-hint=1G src/calc.jl
}

closure
