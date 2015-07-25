#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script with a POL Call"

echo "Legacy script"
POL_SetupWindow_Init

export WINEPREFIX="/tmp/prefixTmp"
POL_Call POL_Install_winhttp

POL_SetupWindow_Close
exit