#!/bin/sh

stop_and_remove_container() {
    docker stop ${CALC_CONTAINER_NAME}
    docker rm ${CALC_CONTAINER_NAME}
}

launch_container() {
    local DISTRO=${1}
    local IMPLEMENTATION=${2}
    docker run -d --name=${CALC_CONTAINER_NAME} -p 8080:8080 aashipov/calc:${DISTRO}-${IMPLEMENTATION}
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    local DISTRO=debian
    local CALC_CONTAINER_NAME=calc

    local IMPLEMENTATIONS="pure-java tomcat netty spring-boot-web spring-boot-webflux quarkus ktor helidon-se dotnet rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket cpp-Crow cpp-poco go-pure go-fasthttp python-fastapi"
    for implementation in ${IMPLEMENTATIONS}
    do
        stop_and_remove_container
        launch_container ${DISTRO} ${implementation}
        printf "\n${implementation}\n"
        sleep 5s
        set -e
        ./calc-test.sh
        set +e
    done
    stop_and_remove_container
}

closure
