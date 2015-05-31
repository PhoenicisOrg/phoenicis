#!/usr/bin/env bash

# Copyright (C) 2007-2011 PlayOnLinux Team
# Copyright (C) 2007-2011 Pâris Quentin
# Copyright (C) 2015 Pâris Quentin

# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

# IMPORTANT
# ---------
# Please note that this library is deprecated and only intended for PlayOnLinux v4 backward compatibility


POL_SetupWindow_Init ()
{
    # Open PlayOnLinux setup window. Should be use only once in a script !
    # Needed for POL_SetupWindow_* functions
    # Usage: POL_SetupWindow_Init

    local arg1
    local arg2
    local arg3
    [ "$1" = "--protect" ] && arg3="protect" && shift

    [ "$POL_SetupWindow_TopImage" = "" ] && arg1="$1" || arg1="$POL_SetupWindow_TopImage"
    [ "$POL_SetupWindow_LeftImage" = "" ] && arg2="$2" || arg2="$POL_SetupWindow_LeftImage"

    [ "$arg1" = "" ] && arg1="None"
    [ "$arg2" = "" ] && arg2="None"
    [ "$arg3" = "" ] && arg3="None"

    echo "$POL_COOKIE	POL_SetupWindow_Init	$$	$arg1	$arg2	$arg3" | toPythonPipe

    export SETUPWINDOW_INIT="true"
}

POL_SetupWindow_message ()
{
    # Shows a simple message
    # Usage POL_SetupWindow_message [message] [title]
    echo "$POL_COOKIE	POL_SetupWindow_message	$$	$(POL_EscapeTab "$1")	$(POL_EscapeTab "$2")" | toPythonPipe
}

POL_SetupWindow_presentation ()
{
    # Default presentation of a script
    # Usage: POL_SetupWindow_presentation [Program's name] [Program's editor] [Editor's url] [Scriptor's name] [Prefix's name]
    [ "$3" = "" ] || url="($3)" 
    POL_SetupWindow_free_presentation "Welcome to $APPLICATION_TITLE Installation Wizard." "This wizard will help you install $1 on your computer.\n\nThis program was created by: $2\n$url\n\nThis installation program is provided by: $4\n\n$1 will be installed in: $POL_USER_ROOT/wineprefix/$5\n\n$APPLICATION_TITLE is not responsible for anything that might happen as a result of using these scripts.\n\nClick Next to start"

}

POL_SetupWindow_free_presentation ()
{
    # Free presentation for a script
    # Usage POL_SetupWindow_free_presentation [title] [message]
    
    echo "$POL_COOKIE	POL_SetupWindow_free_presentation	$$	$(POL_Untab "$1")	$(POL_Untab "$2")" | toPython
}

POL_SetupWindow_Close ()
{
    # Close PlayOnLinux setup window.
    # After this command, POL_SetupWindow_* functions won't work
    # Should be used at the end of the script if POL_SetupWindow_Init has been called
    # Usage: POL_SetupWindow_Close

    echo "$POL_COOKIE	POL_SetupWindow_Close	$$" | toPythonPipe

    export SETUPWINDOW_INIT="false"
    sleep 2
}

POL_SetupWindow_textbox ()
{
    # Shows a text box
    # Usage: POL_SetupWindow_textbox [message] [title (ignored)] [default value]
    # Result is sent in $APP_ANSWER variable

    APP_ANSWER="$(echo "$POL_COOKIE	POL_SetupWindow_textbox	$$	$(POL_EscapeTab "$1")	$(POL_EscapeTab "$3")" | ncns "$POL_HOST" "$POL_PORT")"
}

POL_SetupWindow_menu ()
{
    # Shows a menu
    # Usage: POL_SetupWindow_menu [message] [title (ignored)] [list] [separator]
    # Result is sent in $APP_ANSWER variable

    APP_ANSWER="$(echo "$POL_COOKIE	POL_SetupWindow_menu	$$	$(POL_EscapeTab "$1")	$(POL_EscapeTab "$3")	$(POL_EscapeTab "$4")" | ncns "$POL_HOST" "$POL_PORT")"
}

POL_SetupWindow_wait ()
{
    # Wait for next POL_SetupWindow_ command
    # Usage POL_SetupWindow_wait_next_signal [message] [title]

    echo "$POL_COOKIE	POL_SetupWindow_wait	$$	$(POL_Untab "$1")	$(POL_Untab "$2")" | toPythonPipe
}

POL_SetupWindow_wait_next_signal ()
{
    # POL_SetupWindow_wait, PlayOnLinux v3 script compatibility
    POL_SetupWindow_wait "$@"
}

