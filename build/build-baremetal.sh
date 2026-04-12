#!/bin/sh

# Builds everything locally.

adapters() {
    local IMPLEMENTATIONS="c-exprtk-adapter java-exprtk-adapter rs_expr_adapter"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}/release.sh
    done
}

cpp_flavors() {
    local IMPLEMENTATIONS="cpp-Crow cpp-poco"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}/build.sh
    done
}

jvm_flavors() {
    local IMPLEMENTATIONS="java-pure java-tomcat java-undertow java-jetty java-netty java-spring-boot-web java-spring-boot-webflux java-quarkus java-quarkus-reactive java-ktor java-helidon-se java-scala3-zio-http java-scala3-cask java-micronaut java-Vertx"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        mvn clean package
    done
}

go_flavors(){
    local IMPLEMENTATIONS="go-pure go-fasthttp go-echo go-gin"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        rm -rf app app-debug
        go test
        go build -o app
    done
}

rust_flavors(){
    local IMPLEMENTATIONS="rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        #cargo clean
        cargo test
        cargo build --release
    done
}

dotnet_flavor(){
    local IMPLEMENTATIONS="dotnet"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        dotnet test
        dotnet publish -c Release
    done
}

python_flavor(){
    local IMPLEMENTATIONS="python-fastapi python-flask python-sanic python-tornado"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        ./build.sh
    done
}

nodejs_flavors() {
    local IMPLEMENTATIONS="nodejs-expressjs nodejs-nestjs nodejs-pure"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        ./build.sh
    done
}

alternative_js_flavors() {
    local IMPLEMENTATIONS="bun-pure deno-oak deno-pure"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        ./build.sh
    done
}

closure() {
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}/..

    local SOURCE_TREE_ROOT=$(pwd)
    cd ${SOURCE_TREE_ROOT}

    set -e

    adapters
    cpp_flavors
    jvm_flavors
    go_flavors
    rust_flavors
    dotnet_flavor
    python_flavor
    nodejs_flavors
    alternative_js_flavors
}

closure
