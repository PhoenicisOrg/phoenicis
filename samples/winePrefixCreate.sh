#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script with a wineprefix creation"

echo "Legacy script"
POL_SetupWindow_Init

POL_Wine_SelectPrefix "TestPrefix"

POL_Wine_PrefixCreate "1.7.35"
POL_Wine winecfg
POL_Wine winecfg arg1 arg2 arg3

POL_SetupWindow_Close
exit