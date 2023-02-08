#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
SRC_DIR="${SCRIPT_DIR}/src"
BUILD_DIR="${SCRIPT_DIR}/build"

if [[ ! -d $BUILD_DIR ]]; then
    mkdir "$BUILD_DIR"
fi

cd "$BUILD_DIR"
javac -d . $SRC_DIR/*.java

PROGRAM="$1"
if [[ -z $PROGRAM ]]; then
    echo "Empty program argument!"
    exit 1
fi

java $PROGRAM



