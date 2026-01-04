#!/bin/sh

closure() {
    set -e
    # https://stackoverflow.com/a/1482133
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}
    npm install
    npm run format
    npm run lint
    npm run build
    npm prune --production --omit=dev
    cp dist/main.js ./server.js
}

closure
