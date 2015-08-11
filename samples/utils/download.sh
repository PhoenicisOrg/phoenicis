#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script with a menu"

echo "Legacy script"
POL_SetupWindow_Init

POL_Download "http://download.spotify.com/Spotify%20Installer.exe" ""

POL_SetupWindow_Close
exit