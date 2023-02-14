#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
SRC_DIR="${SCRIPT_DIR}/src"
DIST_DIR="${SCRIPT_DIR}/dist"

if [[ ! -d $DIST_DIR ]]; then
    echo "$DIST_DIR doesn't exist; creating..."
    mkdir "$DIST_DIR"
fi

cp -r "$SRC_DIR" "$DIST_DIR"
rm $DIST_DIR/src/*.class
