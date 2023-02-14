#!/usr/bin/env bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
DIST_DIR="$SCRIPT_DIR"
DIST_BUILD_DIR="${DIST_DIR}/build"
DIST_SRC_DIR="${DIST_DIR}/src"

if [[ ! -d $DIST_SRC_DIR ]]; then
    echo "${DIST_DIR} doesn't exist!" 1>&2
    exit 1
fi

if [[ ! -d $DIST_BUILD_DIR ]]; then
    mkdir $DIST_BUILD_DIR
fi

SOURCE_FILES=("TCPServer.java"
              "TCPClient.java"
              "TCPServerRouter.java"
              "SThread.java"
              "SynchronizedRollingAverage.java")
for file in "${SOURCE_FILES[@]}"; do
    full_file_path="$DIST_SRC_DIR/$file"
    if [[ ! -f $full_file_path ]]; then
        echo "${full_file_path} doesn't exist!" 1>&2
        exit 1
    fi
done

javac -d "$DIST_BUILD_DIR" $DIST_SRC_DIR/*.java
if [[ $? != 0 ]]; then
    echo "Compile failed." 1>&2
    exit 1
fi

PROGRAMS=("TCPServerRouter"
          "TCPServer"
          "TCPClient")
SCRIPT_TMP_DIR=$(mktemp -d -t -p /tmp)
for program in "${PROGRAMS[@]}"; do
    manifest_addition_file="${SCRIPT_TMP_DIR}/Manifest-${program}.txt"
    echo "Main-Class: ${program}" > $manifest_addition_file
    jar cvfm "${program}.jar" "${manifest_addition_file}" -C "${DIST_BUILD_DIR}" .
done

                
