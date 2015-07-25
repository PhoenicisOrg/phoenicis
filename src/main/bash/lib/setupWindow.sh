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

    toPython "POL_SetupWindow_Init" "$arg1"    "$arg2"    "$arg3"

    export SETUPWINDOW_INIT="true"
}

POL_SetupWindow_message ()
{
    # Shows a simple message
    # Usage POL_SetupWindow_message [message] [title]
    
    toPython "POL_SetupWindow_message" "$1" "$2"
}

POL_SetupWindow_browse ()
{
    # Shows a text box with a browse button
    # Usage: POL_SetupWindow_browse [message] [title] [default value] [Supported file types]
    # Result is sent in $APP_ANSWER variable

    APP_ANSWER="$(toPythonRet "POL_SetupWindow_menu" "$1" "$2" "$3" "$PWD" "$4")"
}

POL_SetupWindow_presentation ()
{
    # Default presentation of a script
    # Usage: POL_SetupWindow_presentation [Program's name] [Program's editor] [Editor's url] [Scriptor's name] [Prefix's name]
    [ "$3" = "" ] || url="($3)" 
    toPython "POL_SetupWindow_presentation" "$1" "$2" "$3" "$4" "$5"
}

POL_SetupWindow_free_presentation ()
{
    # Free presentation for a script
    # Usage POL_SetupWindow_free_presentation [title] [message]
    
    toPython "POL_SetupWindow_free_presentation" "$1" "$2"
}

POL_SetupWindow_question()
{
    # FIXME
    # Shows a yes/no question
    # Usage: POL_SetupWindow_question [message] [title]
    # Result is sent in $APP_ANSWER variable (TRUE or FALSE)

	POL_SetupWindow_menu "$1" "$2" "Yes~No" "~"
	[ "$APP_ANSWER" = "Yes" ] && APP_ANSWER="TRUE" || APP_ANSWER="FALSE"
}


POL_SetupWindow_Close ()
{
    # Close PlayOnLinux setup window.
    # After this command, POL_SetupWindow_* functions won't work
    # Should be used at the end of the script if POL_SetupWindow_Init has been called
    # Usage: POL_SetupWindow_Close

    toPython "POL_SetupWindow_Close"

    export SETUPWINDOW_INIT="false"
    sleep 2
}

POL_SetupWindow_textbox ()
{
    # Shows a text box
    # Usage: POL_SetupWindow_textbox [message] [title (ignored)] [default value]
    # Result is sent in $APP_ANSWER variable
    APP_ANSWER="$(toPythonRet "POL_SetupWindow_textbox" "$1" "$2" "$3")"
}

POL_SetupWindow_menu ()
{
    # Shows a menu
    # Usage: POL_SetupWindow_menu [message] [title (ignored)] [list] [separator]
    # Result is sent in $APP_ANSWER variable


    APP_ANSWER="$(toPythonRet "POL_SetupWindow_menu" "$1" "$3" "$4")"
}

POL_SetupWindow_wait ()
{
    # Wait for next POL_SetupWindow_ command
    # Usage POL_SetupWindow_wait_next_signal [message] [title]

    toPython "POL_SetupWindow_wait" "$1" "$2"
}

POL_SetupWindow_wait_next_signal ()
{
    # POL_SetupWindow_wait, PlayOnLinux v3 script compatibility
    POL_SetupWindow_wait "$@"
}

POL_SetupWindow_licence ()
{
    # Shows a licence file, and force the user to accept it to continue
    # Usage POL_SetupWindow_licence [message] [title] [licence's file]
    toPython "POL_SetupWindow_licence" "$1" "$2" "$3"
}

POL_SetupWindow_icon_menu ()
{
    POL_SetupWindow_menu "$@"
}

POL_SetupWindow_InstallMethod()
{
    # Shows a list of install methods
    # Usage: POL_SetupWindow_InstallMethod [List]
    # Elements in list are separated by a coma
    # Accepted methods are STEAM, STEAM_DEMO, LOCAL, CD, DVDROM, DOWNLOAD, ORIGIN, ORIGIN_DEMO, DESURA, DESURA_DEMO

    [ "$1" = "" ] && POL_Debug_Fatal "No method in list"
    STR=""
    ICO=""
    LNG_STEAM="$(eval_gettext "Use Steam Store version")"
    LNG_STEAM_DEMO="$(eval_gettext "Use Steam Store demo version")"
    LNG_DESURA="$(eval_gettext "Use Desura Store version")"
    LNG_DESURA_DEMO="$(eval_gettext "Use Desura Store demo version")"
    LNG_ORIGIN="$(eval_gettext "Use Origin Store version")"
    LNG_ORIGIN_DEMO="$(eval_gettext "Use Origin Store demo version")"
    
    LNG_LOCAL="$(eval_gettext "Use a setup file in my computer")"
    LNG_CDROM="$(eval_gettext "Use CD-ROM(s)")"
    LNG_DVD="$(eval_gettext "Use DVD-ROM(s)")"
    LNG_DOWNLOAD="$(eval_gettext "Download the program")"
    
    if [ ! "$(printf "$1" | grep LOCAL)" = "" ] 
    then 
        STR="$STR~$LNG_LOCAL"
        ICO="$ICO~browse.png"
    fi
    if [ ! "$(printf "$1" | grep CD)" = "" ] 
    then 
        STR="$STR~$LNG_CDROM"
        ICO="$ICO~cdrom.png"
    fi
    if [ ! "$(printf "$1" | grep DVD)" = "" ] 
    then 
        STR="$STR~$LNG_DVD"
        ICO="$ICO~cdrom.png"
    fi
    if [ ! "$(printf "$1" | grep STEAM)" = "" ] 
    then 
        STR="$STR~$LNG_STEAM"
        ICO="$ICO~download.png"
    fi
    if [ ! "$(printf "$1" | grep STEAM_DEMO)" = "" ] 
    then 
        STR="$STR~$LNG_STEAM_DEMO"
        ICO="$ICO~download.png"
    fi
    
    if [ ! "$(printf "$1" | grep DESURA)" = "" ] 
    then 
        STR="$STR~$LNG_DESURA"
        ICO="$ICO~download.png"
    fi
    if [ ! "$(printf "$1" | grep DESURA_DEMO)" = "" ] 
    then 
        STR="$STR~$LNG_DESURA_DEMO"
        ICO="$ICO~download.png"
    fi
    
    if [ ! "$(printf "$1" | grep ORIGIN)" = "" ] 
    then 
        STR="$STR~$LNG_ORIGIN"
        ICO="$ICO~download.png"
    fi
    if [ ! "$(printf "$1" | grep ORIGIN_DEMO)" = "" ] 
    then 
        STR="$STR~$LNG_ORIGIN_DEMO"
        ICO="$ICO~download.png"
    fi
    
    
    if [ ! "$(printf "$1" | grep DOWNLOAD)" = "" ] 
    then 
        STR="$STR~$LNG_DOWNLOAD"
        ICO="$ICO~download.png"
    fi
    
    STR="${STR:1}"
    ICO="${ICO:1}"
    
    mkdir -p "$POL_USER_ROOT/tmp/cache/icons/InstallMethod"
    cp "$PLAYONLINUX/resources/images/icones/browse.png" "$POL_USER_ROOT/tmp/cache/icons/InstallMethod"
    cp "$PLAYONLINUX/resources/images/icones/cdrom.png" "$POL_USER_ROOT/tmp/cache/icons/InstallMethod"
    cp "$PLAYONLINUX/resources/images/icones/download.png" "$POL_USER_ROOT/tmp/cache/icons/InstallMethod"
    
    POL_SetupWindow_icon_menu "$(eval_gettext "Please choose an installation method")" "$TITLE"  "$STR" "~" "$POL_USER_ROOT/tmp/cache/icons/InstallMethod" "$ICO"
    
    # Si l'utilisateur n'a rien choisi
    if [ "$APP_ANSWER" = "" ]
    then
        POL_SetupWindow_InstallMethod "$@"
        return
    fi
    
    [ "$APP_ANSWER" = "$LNG_LOCAL" ] && INSTALL_METHOD="LOCAL"
    [ "$APP_ANSWER" = "$LNG_STEAM" ] && INSTALL_METHOD="STEAM"
    [ "$APP_ANSWER" = "$LNG_STEAM_DEMO" ] && INSTALL_METHOD="STEAM_DEMO"
    [ "$APP_ANSWER" = "$LNG_DESURA" ] && INSTALL_METHOD="DESURA"
    [ "$APP_ANSWER" = "$LNG_DESURA_DEMO" ] && INSTALL_METHOD="DESURA_DEMO"
    [ "$APP_ANSWER" = "$LNG_ORIGIN" ] && INSTALL_METHOD="ORIGIN"
    [ "$APP_ANSWER" = "$LNG_ORIGIN_DEMO" ] && INSTALL_METHOD="ORIGIN_DEMO"
    [ "$APP_ANSWER" = "$LNG_DOWNLOAD" ] && INSTALL_METHOD="DOWNLOAD"
    [ "$APP_ANSWER" = "$LNG_DVD" ] && INSTALL_METHOD="DVD"
    [ "$APP_ANSWER" = "$LNG_CDROM" ] && INSTALL_METHOD="CD"
    
    
    POL_Debug_Message "Install method: $INSTALL_METHOD"
}

