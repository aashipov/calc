#!/bin/sh

# Run JMeter load test via compose
# build openjdkXXbase image first

run_jmeter() {
    local TAKES_COUNT=5
    local TAKE_NO=1
    distro=arch
    implementation="pure-java"
    while [ ${TAKE_NO} -le ${TAKES_COUNT} ]
        do
            rm -rf ${_SCRIPT_DIR}/bin/calc-load-test/result
            rm -rf ${_SCRIPT_DIR}/client
            rm -rf ${_SCRIPT_DIR}/server
            set -e
            docker-compose -f docker-compose-jmeter.yml up
            docker-compose -f docker-compose-jmeter.yml down
            set +e
            cp ${_SCRIPT_DIR}/client/calc-load-test.jtl ${_SCRIPT_DIR}/stats/${implementation}-${distro}-${TAKE_NO}.jtl
            ((TAKE_NO=${TAKE_NO} + 1))
        done

    cd ${_SCRIPT_DIR}/stats
    ./do-stats.sh
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    if grep -q "HOST_TO_TEST=<host-IP>" ${_SCRIPT_DIR}/.env; then
        printf "Replace <host-IP> with host IP in .env variable HOST_TO_TEST and repeat\n"
        exit 1
    fi

    run_jmeter    
}

closure
