#!/bin/sh

# Expects ../calc/<project-tree-with-apps-built>

jvm_flavors() {
    local IMPLEMENTATIONS="java-pure java-tomcat java-undertow java-jetty java-netty java-Vertx java-spring-boot-web java-spring-boot-webflux java-quarkus java-quarkus-reactive java-ktor java-helidon-se java-scala3-zio-http java-scala3-cask java-micronaut"
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
    local IMPLEMENTATIONS="go-pure go-fasthttp go-echo go-gin"
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
        Logging__LogLevel__Default=Warning ${CALC_DIR}/${IMPLEMENTATION}/bin/Release/net10.0/calc &
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
    local IMPLEMENTATIONS="python-fastapi python-flask python-sanic python-tornado"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        ${CALC_DIR}/${IMPLEMENTATION}/run.sh &
        sleep 1s
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f gunicorn
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

nodejs_flavors() {
    local IMPLEMENTATIONS="nodejs-pure nodejs-expressjs nodejs-nestjs"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        cd ${CALC_DIR}/${IMPLEMENTATION}
        node server.js &
        sleep 1s

        cd ${_SCRIPT_DIR}
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f "server.js"
    done
}

deno_flavors() {
    local IMPLEMENTATIONS="deno-pure deno-oak"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        cd ${CALC_DIR}/${IMPLEMENTATION}
        ./calc &
        sleep 1s

        cd ${_SCRIPT_DIR}
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f "calc"
    done
}

ruby_flavor() {
    local IMPLEMENTATIONS="ruby-falcon"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        cd ${CALC_DIR}/${IMPLEMENTATION}
        falcon serve --bind http://0.0.0.0:8080 &
        sleep 1s
        
        cd ${_SCRIPT_DIR}
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f falcon
    done
}

bun_flavor() {
    local IMPLEMENTATIONS="bun-pure"
    local EXECUTABLE_NAME="calc-cluster.ts"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        cd ${CALC_DIR}/${IMPLEMENTATION}
        bun ${EXECUTABLE_NAME} &
        sleep 1s
        
        cd ${_SCRIPT_DIR}
        DISTRO=${DISTRO} IMPLEMENTATION=${IMPLEMENTATION} ./jmeter-runner.sh
        pkill -f ${EXECUTABLE_NAME}
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
    nodejs_flavors
    deno_flavors
    ruby_flavor
    bun_flavor

    ${_SCRIPT_DIR}/stats/do-stats.sh
}

closure
