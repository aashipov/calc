#!/bin/sh

# Expects ../calc/<project-tree-with-apps-built>
# 'Slow by design' ecosystems (like python or js) baremetal throughput harness (what's the point running gunicorn if there's nginx + port offset?)

python_flavors() {
    local IMPLEMENTATIONS="python-fastapi python-flask python-sanic python-tornado python-pyramid"
    local DOT_VENV=.venv
    local PYTHON_3=python3
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n\n${IMPLEMENTATION}\n"
        cd ${CALC_DIR}/${IMPLEMENTATION}
        . ${CALC_DIR}/${IMPLEMENTATION}/${DOT_VENV}/bin/activate
        for HTTP_PORT_SUFFIX in {1..4..1}
        do
            HTTP_PORT=808${HTTP_PORT_SUFFIX} ${PYTHON_3} ${CALC_DIR}/${IMPLEMENTATION}/src/app.py &
        done
        cd ${_SCRIPT_DIR}
        nginx -c ${_SCRIPT_DIR}/nginx.conf
        sleep 5s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f ${PYTHON_3}
        pkill -f nginx
    done
}

nodejs_flavors() {
    local IMPLEMENTATIONS="nodejs-pure nodejs-expressjs nodejs-nestjs"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n\n${IMPLEMENTATION}\n"
        cd ${CALC_DIR}/${IMPLEMENTATION}
        for HTTP_PORT_SUFFIX in {1..4..1}
        do
            HTTP_PORT=808${HTTP_PORT_SUFFIX} node server.js &
        done
        sleep 5s
        cd ${_SCRIPT_DIR}
        haproxy -f haproxy.cfg &
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f "server.js"
        pkill -f haproxy
    done
}

bun_flavors() {
    local IMPLEMENTATIONS="bun-pure"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n\n${IMPLEMENTATION}\n"
        cd ${CALC_DIR}/${IMPLEMENTATION}
        for HTTP_PORT_SUFFIX in {1..4..1}
        do
            HTTP_PORT=808${HTTP_PORT_SUFFIX} bun calc.ts &
        done
        sleep 5s
        cd ${_SCRIPT_DIR}
        haproxy -f haproxy.cfg &
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f "calc.ts"
        pkill -f haproxy
    done
}

deno_flavors() {
    local IMPLEMENTATIONS="deno-pure deno-oak"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n\n${IMPLEMENTATION}\n"
        cd ${CALC_DIR}/${IMPLEMENTATION}
        for HTTP_PORT_SUFFIX in {1..4..1}
        do
            HTTP_PORT=808${HTTP_PORT_SUFFIX} deno --allow-all calc.ts &
        done
        sleep 5s
        cd ${_SCRIPT_DIR}
        nginx -c ${_SCRIPT_DIR}/nginx.conf
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f "calc.ts"
        pkill -f nginx
    done
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    set -e

    local CALC_DIR=${_SCRIPT_DIR}/../calc/

    local DISTRO=arch

    python_flavors
    nodejs_flavors
    bun_flavors
    deno_flavors

    ${_SCRIPT_DIR}/stats/do-stats.sh
}

closure
