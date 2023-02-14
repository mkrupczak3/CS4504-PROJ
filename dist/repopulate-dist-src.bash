#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
SRC_DIR="${SCRIPT_DIR}/../src"
DIST_DIR="${SCRIPT_DIR}/project"

if [[ ! -d $DIST_DIR ]]; then
    echo "$DIST_DIR doesn't exist; creating..."
    mkdir "$DIST_DIR"
fi

echo "Source: $SRC_DIR"
echo "Dest: $DIST_DIR"
cp -r "$SRC_DIR" "$DIST_DIR"
