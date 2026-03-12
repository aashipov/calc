#!/bin/sh

dummy() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=dummy --tag=aashipov/calc:${DISTRO}-dummy
}

openjdk25base() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=openjdk25base --tag=aashipov/calc:${DISTRO}-openjdk25base
}

native() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=native --tag=aashipov/calc:${DISTRO}-native
}

exprtkbuilder() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=exprtkbuilder --tag=aashipov/calc:${DISTRO}-exprtkbuilder
}

openjdk25() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=openjdk25 --tag=aashipov/calc:${DISTRO}-openjdk25
}

dotnet10() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=dotnet10 --tag=aashipov/calc:${DISTRO}-dotnet10
}

javaresult() {
    local IMPLEMENTATIONS="pure-java tomcat undertow jetty netty spring-boot-web spring-boot-webflux quarkus quarkus-reactive ktor helidon-se scala3-zio"
    for implementation in ${IMPLEMENTATIONS}
    do
        ${DOCKER_BUILD_CMD} ${implementation} --file=docker/Dockerfile.${DISTRO} --target=javaresult --tag=aashipov/calc:${DISTRO}-${implementation}
    done
}

dotnet10result() {
    ${DOCKER_BUILD_CMD} dotnet --file=docker/Dockerfile.${DISTRO} --target=dotnet10result --tag=aashipov/calc:${DISTRO}-dotnet
}

rusttoolset() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=rust-toolset --tag=aashipov/calc:${DISTRO}-rust-toolset
}

rust() {
    local IMPLEMENTATIONS="rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket"
    for implementation in ${IMPLEMENTATIONS}
    do
        ${DOCKER_BUILD_CMD} ${implementation} --file=docker/Dockerfile.${DISTRO} --target=rust --tag=aashipov/calc:${DISTRO}-${implementation}
    done
}

cpp() {
    local IMPLEMENTATIONS="cpp-Crow cpp-poco"
    for implementation in ${IMPLEMENTATIONS}
    do
        ${DOCKER_BUILD_CMD} ${implementation} --file=docker/Dockerfile.${DISTRO} --target=cpp --tag=aashipov/calc:${DISTRO}-${implementation}
    done
}

golang() {
    ${DOCKER_BUILD_CMD} . --file=docker/Dockerfile.${DISTRO} --target=golang --tag=aashipov/calc:${DISTRO}-golang
}

golangresult(){
    local IMPLEMENTATIONS="go-pure go-fasthttp"
    for implementation in ${IMPLEMENTATIONS}
    do
        ${DOCKER_BUILD_CMD} ${implementation} --file=docker/Dockerfile.${DISTRO} --target=golangresult --tag=aashipov/calc:${DISTRO}-${implementation}
    done
}

pythonresult() {
    local IMPLEMENTATIONS="python-fastapi"
    for implementation in ${IMPLEMENTATIONS}
    do
        ${DOCKER_BUILD_CMD} ${implementation} --file=docker/Dockerfile.${DISTRO} --target=pythonresult --tag=aashipov/calc:${DISTRO}-${implementation}
    done
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}/..
    
    local SOURCE_TREE_ROOT=$(pwd)
    cd ${SOURCE_TREE_ROOT}

    set -e

    local DISTRO=debian
    local DOCKER_BUILD_CMD="docker build --progress=plain"

    dummy
    openjdk25base
    native
    exprtkbuilder
    openjdk25
    dotnet10
    javaresult
    dotnet10result
    rusttoolset
    rust
    cpp
    golang
    golangresult
    pythonresult
}

closure
