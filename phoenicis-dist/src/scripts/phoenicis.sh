#!/usr/bin/env bash
POL_HOME=$(dirname $0)
CLASSPATH=${CLASSPATH}:$POL_HOME/lib/*

java -classpath "$CLASSPATH" JavaFXApplication "$@"
