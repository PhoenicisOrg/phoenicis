#!/bin/bash

## Dependencies
# Linux:
# - fakeroot
#

VERSION="$1"

if [ "$VERSION" = "" ]; then
    echo "Warning: Version not specified. Reading from pom.xml"
    VERSION="$(cat ../../pom.xml|grep -4 '<parent>'|grep '<version>'|awk -F'[<>]' '/<version>/{print $3}')"
    echo "Using $VERSION"
fi

SCRIPT_PATH="$(dirname "$0")"
cd "$SCRIPT_PATH"
SCRIPT_PATH="$PWD"

[ "$JAVA_HOME" = "" ] && echo "Please set JAVA_HOME" && exit 0

PHOENICIS_OPERATING_SYSTEM="$(uname)"

if [ "$PHOENICIS_OPERATING_SYSTEM" == "Darwin" ]; then
    PHOENICIS_APPTITLE="Phoenicis PlayOnMac"
    JPACKAGER_OS="osx"
    JAR_RELATIVE_PATH="../Java"
fi

if [ "$PHOENICIS_OPERATING_SYSTEM" == "Linux" ]; then
    PHOENICIS_APPTITLE="Phoenicis PlayOnLinux"
    JPACKAGER_OS="linux"
    JAR_RELATIVE_PATH="/usr/share/phoenicis/app"
fi

PHOENICIS_TARGET="$SCRIPT_PATH/../../target"
PHOENICIS_JPACKAGER="$SCRIPT_PATH/../../target/jpackager"
PHOENICIS_RESOURCES="$SCRIPT_PATH/../resources"
PHOENICIS_MODULES="jdk.crypto.ec,java.base,javafx.base,javafx.web,javafx.media,javafx.graphics,javafx.controls,java.naming,java.sql,java.scripting,jdk.internal.vm.ci,org.graalvm.truffle,java.management"
PHOENICIS_RUNTIME_OPTIONS="-XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI --upgrade-module-path=$JAR_RELATIVE_PATH/compiler.jar"
PHOENICIS_JPACKAGER_ARGUMENTS=("-i" "$PHOENICIS_TARGET/lib" "--main-jar" "phoenicis-javafx-$VERSION.jar" "-n" "$PHOENICIS_APPTITLE" "--output" "$PHOENICIS_TARGET/packages/" "--add-modules" "$PHOENICIS_MODULES" "-p" "$PHOENICIS_TARGET/lib/" "--version" "$VERSION" "--jvm-args" "$PHOENICIS_RUNTIME_OPTIONS")

_download_jpackager() {
    mkdir -p "$PHOENICIS_JPACKAGER"
    cd "$PHOENICIS_JPACKAGER"
    wget http://download2.gluonhq.com/jpackager/11/jdk.packager-$JPACKAGER_OS.zip
    unzip jdk.packager-$JPACKAGER_OS.zip
}

jpackager() {
    if [ ! -e "$PHOENICIS_JPACKAGER/jpackager" ]; then
        _download_jpackager
    fi

    "$PHOENICIS_JPACKAGER/jpackager" "$@"
}

package_image() {
    if command -v jpackage >/dev/null 2>&1; then
        JPACKAGE_ARGUMENTS=(
            "--type" "app-image"
            "--input" "$PHOENICIS_TARGET/lib"
            "--main-jar" "phoenicis-javafx-$VERSION.jar"
            "--name" "$PHOENICIS_APPTITLE"
            "--dest" "$PHOENICIS_TARGET/packages/"
            "--add-modules" "$PHOENICIS_MODULES"
            "--module-path" "$PHOENICIS_TARGET/lib/"
            "--app-version" "$VERSION"
            "--java-options" "$PHOENICIS_RUNTIME_OPTIONS"
        )

        if [ "$PHOENICIS_OPERATING_SYSTEM" == "Darwin" ]; then
            JPACKAGE_ARGUMENTS+=("--icon" "$PHOENICIS_RESOURCES/Phoenicis PlayOnMac.icns")
        fi

        jpackage "${JPACKAGE_ARGUMENTS[@]}"
    else
        if [ "$PHOENICIS_OPERATING_SYSTEM" == "Darwin" ]; then
            jpackager create-image --icon "$PHOENICIS_RESOURCES/Phoenicis PlayOnMac.icns" "${PHOENICIS_JPACKAGER_ARGUMENTS[@]}"
        fi

        if [ "$PHOENICIS_OPERATING_SYSTEM" == "Linux" ]; then
            jpackager create-image "${PHOENICIS_JPACKAGER_ARGUMENTS[@]}" --linux-bundle-name "phoenicis-playonlinux"
        fi
    fi
}

find_linux_app_image_dir() {
    local candidates=(
        "$PHOENICIS_TARGET/packages/$PHOENICIS_APPTITLE"
        "$PHOENICIS_TARGET/packages/${PHOENICIS_APPTITLE// /}"
        "$PHOENICIS_TARGET/packages/Phoenicis PlayOnLinux"
        "$PHOENICIS_TARGET/packages/PhoenicisPlayOnLinux"
    )

    for candidate in "${candidates[@]}"; do
        if [ -d "$candidate" ]; then
            echo "$candidate"
            return 0
        fi
    done

    find "$PHOENICIS_TARGET/packages" -mindepth 1 -maxdepth 1 -type d | head -n 1
}

cd "$PHOENICIS_TARGET"
package_image

if [ "$PHOENICIS_OPERATING_SYSTEM" == "Linux" ]; then
    packageName="Phoenicis_$VERSION"
    cd "$PHOENICIS_TARGET"

    appImageDir="$(find_linux_app_image_dir)"

    if [ -z "$appImageDir" ]; then
        echo "Unable to locate generated app image directory in $PHOENICIS_TARGET/packages"
        exit 1
    fi

    rm -rf "$PHOENICIS_TARGET/packages/phoenicis" 2> /dev/null
    mv "$appImageDir" "$PHOENICIS_TARGET/packages/phoenicis"

    rm -rf "$packageName" 2> /dev/null
    mkdir -p "$packageName/DEBIAN/"

    cat << EOF_CONTROL > "$packageName/DEBIAN/control"
Package: phoenicis-playonlinux
Version: $VERSION
Section: misc
Priority: optional
Architecture: all
Depends: openjdk-25-jdk, unzip, wget, xterm | x-terminal-emulator, python3 | python, imagemagick, cabextract, icoutils, p7zip-full, curl, winbind, libc6:i386
Maintainer: PlayOnLinux Packaging <packages@playonlinux.com>
Description: This program is a front-end for wine.
 It permits you to install Windows Games and softwares
 on Linux. It allows you to manage differents virtual hard drive,
 and several wine versions.
 Copyright 2011-2019 PlayOnLinux team <contact@playonlinux.com>
EOF_CONTROL

    mkdir -p "$packageName/usr/share/applications"
    mkdir -p "$packageName/usr/share/pixmaps"
    mkdir -p "$packageName/usr/bin"

    cp -a "$PHOENICIS_TARGET/packages/phoenicis" "$packageName/usr/share/"
    cp -a "$SCRIPT_PATH/../launchers/phoenicis" "$packageName/usr/bin/phoenicis"
    chmod +x "$packageName/usr/bin/phoenicis"

    launcherCompatibilityDir="$packageName/usr/share/phoenicis/bin"
    mkdir -p "$launcherCompatibilityDir"
    cat << 'EOF_LAUNCHER' > "$launcherCompatibilityDir/Phoenicis PlayOnLinux"
#!/bin/sh

exec /usr/bin/phoenicis "$@"
EOF_LAUNCHER
    chmod +x "$launcherCompatibilityDir/Phoenicis PlayOnLinux"

    cp "$SCRIPT_PATH/../resources/Phoenicis.desktop" "$packageName/usr/share/applications"
    cp "$SCRIPT_PATH/../resources/phoenicis.png" "$packageName/usr/share/pixmaps"
    cp "$SCRIPT_PATH/../resources/phoenicis-16.png" "$packageName/usr/share/pixmaps"
    cp "$SCRIPT_PATH/../resources/phoenicis-32.png" "$packageName/usr/share/pixmaps"

    fakeroot dpkg-deb --build "$packageName"
    rm -rf deb
fi
