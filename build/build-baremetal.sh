#!/bin/sh

# Builds everything locally.

build_and_install() {
    for IMPLEMENTATION in ${1}
    do
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        printf '\n%s\n' "${IMPLEMENTATION}"
        make clean
        make install PREFIX=${HOME}/.local
    done
}

build() {
    for IMPLEMENTATION in ${1}
    do
        cd ${SOURCE_TREE_ROOT}/${IMPLEMENTATION}
        printf '\n%s\n' "${IMPLEMENTATION}"
        make clean
        make PREFIX=${HOME}/.local
    done
}

closure() {
    # https://stackoverflow.com/a/1482133
    _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}/..

    SOURCE_TREE_ROOT=$(pwd)
    cd ${SOURCE_TREE_ROOT}

    set -e

    build_and_install "c-exprtk-adapter c-meval-adapter java-expr-adapter"
    build "cpp-Crow cpp-poco cpp-cpprestsdk"
    build "java-pure java-tomcat java-undertow java-jetty java-netty java-spring-boot-web java-spring-boot-webflux java-quarkus java-quarkus-reactive java-ktor java-helidon-se java-scala3-zio-http java-scala3-cask java-micronaut java-Vertx"
    build "go-pure go-fasthttp go-echo go-gin"
    build "rust-actix rust-axum rust-ntex rust-tiny-http rust-rocket"
    build "dotnet"
    build "python-fastapi python-flask python-sanic python-tornado"
    build "nodejs-expressjs nodejs-nestjs nodejs-pure"
    build "bun-pure deno-oak deno-pure"
}

closure
