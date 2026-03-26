#!/bin/sh

# Run baremetal JMeter load test
# 127.0.0.1 jmeter-server{1,2,3} alias will produce three times larger request time.
# No such issue with different host IPs (like dockerized or baremetal multihost)
# Submaximal load of 10k per round will produce cleaner throughput comparison
# 
# sh -c "DISTRO=arch IMPLEMENTATION=cpprestsdk ./jmeter-runner.sh"
# 
# Put following line to /etc/hosts (without opening #) 
# 127.0.0.1 host.to.test

kill_jmeter_leftovers() {
    pkill -f "JMeter"
}

run_jmeter() {
    mkdir -p ./{server,client}
    local TAKES_COUNT=5
    local take_no=1
    while [ ${take_no} -le ${TAKES_COUNT} ]
    do
        kill_jmeter_leftovers
        set -e  
        rm -rf ./stats/${IMPLEMENTATION}-${DISTRO}-${take_no}.jtl
        rm -rf ./client/web-report-${IMPLEMENTATION}-${DISTRO}-${take_no}
        
        ./bin/jmeter -n -t ./bin/calc-load-test/calc-load-test.jmx -l ./stats/${IMPLEMENTATION}-${DISTRO}-${take_no}.jtl -j ./client/jmeter-client-${IMPLEMENTATION}-${DISTRO}-${take_no}.log -e -o ./client/web-report-${IMPLEMENTATION}-${DISTRO}-${take_no}
        
        set +e
        
        ((take_no=${take_no} + 1))
        kill_jmeter_leftovers
    done

    cd ${_SCRIPT_DIR}/stats
    #./do-stats.sh
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

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
