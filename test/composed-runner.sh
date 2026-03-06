#!/bin/sh

# Run JMeter load test via compose

stop_and_remove_container() {
    docker stop ${CALC_CONTAINER_NAME}
    docker rm ${CALC_CONTAINER_NAME}
}

launch_container() {
    printf "\nStarting ${distro}-${implementation}-${TAKE_NO}\n"
    set -e
    docker run -d --name=${CALC_CONTAINER_NAME} -p 8080:8080 aashipov/calc:${distro}-${implementation}
    # Managed memory languages may need some initialization timeout
    sleep 10s
    set +e
}

perform_load_test() {
    rm -rf ${_SCRIPT_DIR}/bin/calc-load-test/result
    rm -rf ${_SCRIPT_DIR}/client
    rm -rf ${_SCRIPT_DIR}/server
    docker-compose -f docker-compose-jmeter.yml up
    docker-compose -f docker-compose-jmeter.yml down
    cp ${_SCRIPT_DIR}/client/calc-load-test.jtl ${_SCRIPT_DIR}/stats/${implementation}-${distro}-${TAKE_NO}.jtl
    ((TAKE_NO=${TAKE_NO} + 1))
}

main() {
    local TAKES_COUNT=5
    local IMPLEMENTATIONS="pure-java tomcat undertow jetty netty spring-boot-web spring-boot-webflux quarkus quarkus-reactive ktor helidon-se dotnet rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket cpp-Crow cpp-poco go-pure go-fasthttp python-fastapi"
    local DISTROS="debian"
    for d in ${DISTROS}
    do
        for implementation in ${IMPLEMENTATIONS}
        do
            docker pull aashipov/calc:${d}-${implementation}
        done
    done
    docker pull aashipov/calc:debian-openjdk25
    for distro in ${DISTROS}
    do
        for implementation in ${IMPLEMENTATIONS}
        do
            stop_and_remove_container
            launch_container
            local TAKE_NO=1
            while [ ${TAKE_NO} -le ${TAKES_COUNT} ]
            do
                perform_load_test
            done
            stop_and_remove_container
        done
    done

    cd ${_SCRIPT_DIR}/stats
    ./do-stats.sh
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    
    if [ ! -f ${_SCRIPT_DIR}/.env ]
    then
        printf ".env file is missing. Make one out of .env.template\n"
        exit 1
    fi

    if grep -q "HOST_TO_TEST=<host-IP>" ${_SCRIPT_DIR}/.env
    then
        printf "Replace <host-IP> with host IP in .env variable HOST_TO_TEST and repeat\n"
        exit 1
    fi

    local CALC_CONTAINER_NAME=calc

    main
}

closure
