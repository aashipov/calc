#!/bin/sh

# Run JMeter load test via compose
# build openjdkXXbase image first

run_jmeter() {
    local TAKES_COUNT=5
    local TAKE_NO=1
    while [ ${TAKE_NO} -le ${TAKES_COUNT} ]
    do
        rm -rf ${_SCRIPT_DIR}/bin/calc-load-test/result
        rm -rf ${_SCRIPT_DIR}/client
        rm -rf ${_SCRIPT_DIR}/server
        set -e
        docker-compose -f docker-compose-jmeter.yml up
        docker-compose -f docker-compose-jmeter.yml down
        set +e
        cp ${_SCRIPT_DIR}/client/calc-load-test.jtl ${_SCRIPT_DIR}/stats/${IMPLEMENTATION}-${DISTRO}-${TAKE_NO}.jtl
        ((TAKE_NO=${TAKE_NO} + 1))
    done

    cd ${_SCRIPT_DIR}/stats
    #./do-stats.sh
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    
    set -x

    # Standalone: uncomment two variables, change accordingly
    #local DISTRO=arch
    #local IMPLEMENTATION=pure-java

    if [ -z ${DISTRO} ]
    then
        printf "Define DISTRO variable\n"
        exit 1
    fi

    if [ -z ${IMPLEMENTATION} ]
    then
        printf "Define IMPLEMENTATION variable\n"
        exit 1
    fi

    if [ ! -f ${_SCRIPT_DIR}/.env ]
    then
        printf ".env file is missing. Making one out of .env.template\n"
        cp .env.template .env
    fi

    if grep -q "HOST_TO_TEST=<host-IP>" ${_SCRIPT_DIR}/.env
    then
        printf "Replace <host-IP> with host IP in .env variable HOST_TO_TEST and repeat\n"
        exit 1
    fi

    run_jmeter
}

closure
