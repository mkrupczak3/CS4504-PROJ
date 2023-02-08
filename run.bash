#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
SRC_DIR="${SCRIPT_DIR}/src"
BUILD_DIR="${SCRIPT_DIR}/build"

cd $SCRIPT_DIR

if [[ ! -d $BUILD_DIR ]]; then
    mkdir "$BUILD_DIR"
fi

javac -d "$BUILD_DIR" $SRC_DIR/*.java

PROGRAM="$1"
if [[ -z $PROGRAM ]]; then
    echo "Empty program argument!"
    exit 1
fi

java -cp "$BUILD_DIR" "$PROGRAM"



