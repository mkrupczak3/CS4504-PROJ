#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
SRC_DIR="${SCRIPT_DIR}/src"
BUILD_DIR="${SCRIPT_DIR}/build"

cd $SCRIPT_DIR

if [[ ! -d $BUILD_DIR ]]; then
    mkdir "$BUILD_DIR"
fi

javac -d "$BUILD_DIR" $SRC_DIR/*.java
if [[ $? != 0 ]]; then
    echo "Compile failed."
    exit 1
fi

PROGRAM="$1"
if [[ -z $PROGRAM ]]; then
    echo "Empty program argument!"
    exit 1
fi

# remove files from server so payload delivery can be tested
if [[ "$PROGRAM" != "TCPClient" ]]; then
    rm *.txt *.mp3 *.mp4
fi

# run Java program with all arguments passed to this shell
if [[ $# -eq 1 ]]; then
    java -cp "$BUILD_DIR" "$PROGRAM"
else
    java -cp "$BUILD_DIR" "$PROGRAM" "${@:2}"
fi
