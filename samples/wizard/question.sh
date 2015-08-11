#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script with a menu"

echo "Legacy script"
POL_SetupWindow_Init

POL_SetupWindow_question "Message" "Title"

POL_SetupWindow_message "$APP_ANSWER"
POL_SetupWindow_Close
exit