#!/usr/bin/env bash
POL_HOME="$(dirname "$0")"
cd "$POL_HOME"
POL_HOME="$PWD"

java --module-path "$POL_HOME/lib" -m org.phoenicis.javafx/org.phoenicis.javafx.JavaFXApplication "$@"
