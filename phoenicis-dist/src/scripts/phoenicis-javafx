#!/usr/bin/env bash
POL_HOME="$(dirname "$0")"
cd "$POL_HOME"
POL_HOME="$PWD"
CLASSPATH=${CLASSPATH}:$POL_HOME/lib/*

java -classpath "$CLASSPATH" org.phoenicis.javafx.JavaFXApplication "$@"
