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



POL_System_SetArch()
{
    # Set the architecture to use for the current script
    # Usage: POL_System_SetArch (auto|x86|amd64)
    # If amd64 is specified and not supported, the script will end

    # $1 = Auto, x86, amd64
    # $2 = detected, if the result of an automatic selection

    if [ ! "$1" == "x86" ] && [ ! "$1" == "amd64" ]
    then
        [ "$AMD64_COMPATIBLE" == "True" ] && export POL_ARCH="amd64" || export POL_ARCH="x86"
    fi
    if [ "$1" == "x86" ]
    then
        export POL_ARCH="x86"
    fi
    if [ "$1" == "amd64" ]
    then
        [ "$AMD64_COMPATIBLE" == "True" ] && export POL_ARCH="amd64" || POL_Debug_Fatal "amd64 is not supported by your system"
    fi

    [ "$2" = "detected" ] || POL_USER_ARCH="$POL_ARCH"

    POL_Debug_Message "POL_ARCH set to $POL_ARCH"
}


clean_wineprefix()
{
	# Detect if a prefix is still use or not.
	# It not, ask the user if he/she wants to delete it
	# Usage: clean_wineprefix (--non-interactive) /path/of/the/prefix

	local INTERACTIVE=1
	if [ "$1" = "--non-interactive" ]; then
		unset INTERACTIVE
		shift
	fi
 	local wineprefix="$1"
    wineprefix=${wineprefix//"//"/"/"}

	# Done on purpose, do not change!
	[ "$wineprefix" = "$POL_USER_ROOT/.PlayOnLinux/wineprefix/" ] && (throw ; exit)
	[ "$wineprefix" = "$POL_USER_ROOT/Library/PlayOnMac/" ] && (throw ; exit)

	# Done without using $POL_USER_ROOT on purpose too; If it's not set or set to a wrong value it could do a lot of damage
	local TEST
	[ "$POL_OS" = "Linux" ] && TEST=${wineprefix//".PlayOnLinux/wineprefix/"/""}
	[ "$POL_OS" = "FreeBSD" ] && TEST=${wineprefix//".PlayOnBSD/wineprefix/"/""}
	[ "$POL_OS" = "Mac" ] && TEST=${wineprefix//"Library/PlayOnMac/wineprefix/"/""}

	[ "$TEST" = "" ] && exit
	if [ "$TEST" = "$wineprefix" ]
	then
		POL_Debug_Error "The directory is not in $APPLICATION_TITLE"
	else
		local used=""
		local OLDIFS="$IFS"
                IFS=$'\n'
		cd "$POL_USER_ROOT/shortcuts/" &&
		for shortcut in *
		do
			[ "$(detect_wineprefix "$shortcut")" = "$wineprefix" ] && used=1
		done
                IFS="$OLDIFS"
		if [ "$used" = 1 ]; then
 			POL_Debug_Warning "$1 is still in use"
		else
			local prefix=$(basename "$wineprefix")
			[ "$INTERACTIVE" ] && POL_SetupWindow_question "$(eval_gettext "Do you want to delete the virtual drive:")\n\n$prefix" "$TITLE"
			if [ "$APP_ANSWER" = "TRUE" -o -z "$INTERACTIVE" ]
			then
				rm -rf "$1"
			fi
		fi
	fi
}
