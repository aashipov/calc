#!/bin/sh

# Run JMeter load test via compose

check_cgroup_v1_enabled() {
    mount | grep -q "cgroup "
    if [ $? -ne 0 ]
    then
        printf "Enable cgroup v1 for java 11 not to OOM in containers\n"
        exit 1
    fi
}

stop_and_remove_container() {
    docker stop ${CALC_CONTAINER_NAME}
    docker rm ${CALC_CONTAINER_NAME}
}

launch_container() {
    local DISTRO=${1}
    local IMPLEMENTATION=${2}
    docker run -d --name=${CALC_CONTAINER_NAME} -p 8080:8080 aashipov/calc:${DISTRO}-${IMPLEMENTATION}
}

main() {
    local TAKES_COUNT=5
    local IMPLEMENTATIONS="pure tomcat spring-boot-web spring-boot-webflux ktor nodejs dotnet"
    local DISTROS="debian"
    for d in ${DISTROS}
    do
        for implementation in ${IMPLEMENTATIONS}
        do
            docker pull aashipov/calc:${d}-${implementation}
        done
    done
    docker pull aashipov/calc:debian-openjdk17
    for distro in ${DISTROS}
    do
        for implementation in ${IMPLEMENTATIONS}
        do
            local TAKE_NO=1
            while [ ${TAKE_NO} -le ${TAKES_COUNT} ]
            do
                stop_and_remove_container
                launch_container ${distro} ${implementation}
                rm -rf ${_SCRIPT_DIR}/bin/calc-load-test/result
                rm -rf ${_SCRIPT_DIR}/client
                rm -rf ${_SCRIPT_DIR}/server
                docker-compose -f docker-compose-jmeter.yml up
                docker-compose -f docker-compose-jmeter.yml down
                cp ${_SCRIPT_DIR}/client/calc-load-test.jtl ${_SCRIPT_DIR}/stats/${implementation}-${distro}-${TAKE_NO}.jtl
                ((TAKE_NO=${TAKE_NO} + 1))
                stop_and_remove_container
            done
        done
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

    local CALC_CONTAINER_NAME=calc

    check_cgroup_v1_enabled
    main
}

closure
