#!/bin/sh

closure() {
    local _SCRIPT_DIR=$(dirname -- "$(readlink -f -- "$0")")
    cd ${_SCRIPT_DIR}

    julia --project=. --color=yes --check-bounds=yes -e 'using Pkg; Pkg.activate(".")'
    julia --project=. --color=yes --check-bounds=yes -e 'using Pkg; Pkg.build()'
    #Pkg.test() # (coverage=true)
}

closure
