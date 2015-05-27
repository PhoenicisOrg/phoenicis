#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script"

echo "Test"
POL_SetupWindow_Init
POL_SetupWindow_message "Test"
POL_SetupWindow_message "Test 2"
POL_SetupWindow_message "Test 3"
POL_Download "http://www.playonlinux.com/images/logos/logo96.png" "a857e55353968fa77efa4ddae7d8853f"

throw "Cancel this script please"

POL_SetupWindow_message "This message should never appear"
POL_SetupWindow_Close
exit