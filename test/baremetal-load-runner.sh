#!/bin/sh

# Expects ../calc/<project-tree-with-apps-built>

jvm_flavors() {
    local IMPLEMENTATIONS="pure-java tomcat undertow jetty netty Vertx micronaut spring-boot-web spring-boot-webflux quarkus quarkus-reactive ktor helidon-se helidon-mp scala3-zio-http scala3-cask"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        java -jar ${CALC_DIR}/${IMPLEMENTATION}/target/calc-shaded.jar &
        sleep 10s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f calc-shaded.jar
        sleep 10s
    done
}

go_flavors(){
    local IMPLEMENTATIONS="go-pure go-fasthttp"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        ${CALC_DIR}/${IMPLEMENTATION}/app &
        sleep 1s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f app
    done
}

rust_flavors(){
    local IMPLEMENTATIONS="rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        ${CALC_DIR}/${IMPLEMENTATION}/target/release/calc &
        sleep 1s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f calc
    done
}

dotnet_flavor() {
    local IMPLEMENTATIONS="dotnet"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        ${CALC_DIR}/${IMPLEMENTATION}/bin/Release/net10.0/calc &
        sleep 1s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f calc
    done
}

cpp_flavors() {
    local IMPLEMENTATIONS="cpp-cpprestsdk cpp-Crow cpp-poco"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        ${CALC_DIR}/${IMPLEMENTATION}/build/calc &
        sleep 1s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f calc
    done
}

python_flavor() {
    local IMPLEMENTATIONS="python-fastapi"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        ${CALC_DIR}/${IMPLEMENTATION}/run.sh &
        sleep 1s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f run.sh
    done
}

dart_flavor() {
    local IMPLEMENTATIONS="dart-pure"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        cd ${CALC_DIR}/${IMPLEMENTATION}
        dart run &
        sleep 1s
        
        cd ${_SCRIPT_DIR}
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f dart
    done
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    
    set -x

    local CALC_DIR=${_SCRIPT_DIR}/../calc/

    local DISTRO=arch

    jvm_flavors
    go_flavors
    rust_flavors
    dotnet_flavor
    cpp_flavors
    python_flavor
    dart_flavor

    ${_SCRIPT_DIR}/stats/do-stats.sh
}

closure
