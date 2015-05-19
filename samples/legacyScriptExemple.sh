#!/bin/bash
[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"

TITLE="Legacy script"

echo "Test"
POL_SetupWindow_Init
POL_Download "http://files.metaquotes.net/metaquotes.software.corp/mt4/mt4setup.exe" "e42ebc9ebc2d8975de5a0c94386d1a6a"
POL_SetupWindow_message "Test"
POL_SetupWindow_message "Test 2"
POL_SetupWindow_message "Test 3"
POL_SetupWindow_Close
exit