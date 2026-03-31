#!/bin/sh

# Builds everything locally.

c_exprtk_adapter_and_cpp() {
    local IMPLEMENTATIONS="c-exprtk-adapter cpp-Crow cpp-poco"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}/release.sh
    done
}

jvm_flavors() {
    local IMPLEMENTATIONS="java-pure tomcat undertow jetty netty spring-boot-web spring-boot-webflux quarkus quarkus-reactive ktor helidon-se scala3-zio"
    for IMPLEMENTATION in ${IMPLEMENTATIONS}
    do
        printf "\n${IMPLEMENTATION}\n"
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        mvn clean package
    done
}

go_flavors(){
    local IMPLEMENTATIONS="go-pure go-fasthttp"
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
    local IMPLEMENTATIONS="python-fastapi"
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

    c_exprtk_adapter_and_cpp
    jvm_flavors
    go_flavors
    rust_flavors
    dotnet_flavor
    python_flavor
}

closure
