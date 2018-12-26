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
fi

if [ "$PHOENICIS_OPERATING_SYSTEM" == "Linux" ]; then
    PHOENICIS_APPTITLE="Phoenicis PlayOnLinux"
    JPACKAGER_OS="linux"
fi

PHOENICIS_TARGET="$SCRIPT_PATH/../../target"
PHOENICIS_JPACKAGER="$SCRIPT_PATH/../../target/jpackager"
PHOENICIS_RESOURCES="$SCRIPT_PATH/../resources"
PHOENICIS_MODULES="java.base,javafx.base,javafx.media,javafx.graphics,javafx.controls,java.naming,java.sql"
PHOENICIS_JPACKAGER_ARGUMENTS=("-i" "$PHOENICIS_TARGET/lib" "--main-jar" "phoenicis-javafx-$VERSION.jar" "-n" "$PHOENICIS_APPTITLE" "--output" "$PHOENICIS_TARGET/packages/" "--add-modules" "$PHOENICIS_MODULES" "-p" "$PHOENICIS_TARGET/lib/" "--version" "$VERSION")


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

cd "$PHOENICIS_TARGET"

if [ "$PHOENICIS_OPERATING_SYSTEM" == "Darwin" ]; then
    jpackager create-image --icon "$PHOENICIS_RESOURCES/Phoenicis PlayOnMac.icns" "${PHOENICIS_JPACKAGER_ARGUMENTS[@]}"
fi

if [ "$PHOENICIS_OPERATING_SYSTEM" == "Linux" ]; then
    jpackager create-image "${PHOENICIS_JPACKAGER_ARGUMENTS[@]}"  --linux-bundle-name "phoenicis-playonlinux"
    jpackager create-image deb "${PHOENICIS_JPACKAGER_ARGUMENTS[@]}" --linux-package-deps "unzip, wget, xterm | x-terminal-emulator, imagemagick, cabextract, icoutils, p7zip-full, curl, winbind, libasound2, libc6, libfontconfig1, libfreetype6, libgcc1, libglib2.0-0, libgphoto2-6, libgphoto2-port12, libgstreamer-plugins-base1.0-0, libgstreamer1.0-0, liblcms2-2, libldap-2.4-2, libmpg123-0, libncurses5, libopenal1, libpcap0.8, libpulse0, libtinfo5, libudev1, libx11-6, libxext6, libxml2, ocl-icd-libopencl1, zlib1g, fonts-liberation, fonts-wine, libasound2-plugins, libcapi20-3, libcups2, libdbus-1-3, libgl1, libgl1-mesa-dri, libglu1-mesa, libgnutls30, libgsm1, libgssapi-krb5-2, libjpeg8, libkrb5-3, libodbc1, libosmesa6, libpng16-16, libsane1, libsdl2-2.0-0, libtiff5, libv4l-0, libvulkan1, libxcomposite1, libxcursor1, libxfixes3, libxi6, libxinerama1, libxrandr2, libxrender1, libxslt1.1, libxxf86vm1, libpulse0, libattr1, libva-x11-2, libva-drm2, libgtk-3-0, libasound2:i386, libc6:i386, libfontconfig1:i386, libfreetype6:i386, libgcc1:i386, libglib2.0-0:i386, libgphoto2-6:i386, libgphoto2-port12:i386, libgstreamer-plugins-base1.0-0:i386, libgstreamer1.0-0:i386, liblcms2-2:i386, libldap-2.4-2:i386, libmpg123-0:i386, libncurses5:i386, libopenal1:i386, libpcap0.8:i386, libpulse0:i386, libtinfo5:i386, libudev1:i386, libx11-6:i386, libxext6:i386, libxml2:i386, ocl-icd-libopencl1:i386, zlib1g:i386, libasound2-plugins:i386, libcapi20-3:i386, libcups2:i386, libdbus-1-3:i386, libgl1:i386, libgl1-mesa-dri:i386, libglu1-mesa:i386, libgnutls30:i386, libgsm1:i386, libgssapi-krb5-2:i386, libjpeg8:i386, libkrb5-3:i386, libodbc1:i386, libosmesa6:i386, libpng16-16:i386, libsane1:i386, libsdl2-2.0-0:i386, libtiff5:i386, libv4l-0:i386, libvulkan1:i386, libxcomposite1:i386, libxcursor1:i386, libxfixes3:i386, libxi6:i386, libxinerama1:i386, libxrandr2:i386, libxrender1:i386, libxslt1.1:i386, libxxf86vm1:i386, libpulse0:i386, libattr1:i386, libva-x11-2:i386, libva-drm2:i386, libgtk-3-0:i386" --linux-deb-maintainer "PlayOnLinux Packaging <packages@playonlinux.com>" --linux-bundle-name "phoenicis-playonlinux"
fi


