#!/bin/sh

closure() {
    local BASE_URL="http://0.0.0.0:8080"
    curl ${BASE_URL}/abc
    printf "\n"
    curl -X POST -H "Content-Type: text/plain" --data "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2" ${BASE_URL}
    printf "\n"
    curl -X POST -H "Content-Type: text/plain" --data "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2" ${BASE_URL}/mxparser
    printf "\n"
}

closure
