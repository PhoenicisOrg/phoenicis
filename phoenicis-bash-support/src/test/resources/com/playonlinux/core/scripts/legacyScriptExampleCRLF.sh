#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script"

POL_SetupWindow_Init
POL_SetupWindow_message "Test"
POL_SetupWindow_Close

exit 