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


POL_Download ()
{
    # Download a file and place it in the current directory
    # Usage: POL_Download [URL] [MD5]

    toPython "POL_Download" "$1" "$PWD" "$2"
}


POL_System_TmpCreate()
{
    # Create a temporary directory for a script
    # Usage: POL_System_TmpCreate [tmp_name]

    [ "$1" = "" ] && POL_Debug_Fatal "TmpName not defined !"
    mkdir -p "$POL_USER_ROOT/tmp/$1"
    export POL_System_TmpDir="$POL_USER_ROOT/tmp/$1"
    export POL_System_TmpName="$1"
}

POL_System_TmpDelete()
{
    # Delete the temporary directory created before
    # Usage: POL_System_TmpDelete

    [ ! "$POL_System_TmpName" = "" ] && rm -rf "$POL_USER_ROOT/tmp/$POL_System_TmpName" || POL_Debug_Warning "POL_System_TmpName is not defined !"
}


POL_Call()
{
    # Call a "function" script from PlayOnLinux website
    # Usage: POL_Call [function name]

    POL_Debug_Message "Calling $1"
    if [ ! "$1" = "" ]
    then
        OLD_PC_DIR="$PWD"
        cd "$POL_USER_ROOT/tmp/"
        # Local overriding mechanism
        if [ "$(POL_Config_Read "ALLOW_FUNCTION_OVERRIDES")" = "TRUE" -a -e "$POL_USER_ROOT/configurations/function_overrides/$1" ]; then
            cp "$POL_USER_ROOT/configurations/function_overrides/$1" .
            if [ -z "$NOBUGREPORT" ]; then
                NOBUGREPORT="TRUE"
                POL_SetupWindow_message "$(eval_gettext 'Function override detected, disabling bug reporting')" "$TITLE"
            fi
            POL_Debug_Message "----- Starting function $1 (OVERWRITTEN) -----"
            POL_Debug_LogToPrefix "----- Starting function $1 (OVERWRITTEN) -----"
            source "$@"
            exitcode="$?"
            POL_Debug_Message "----- Ending function $1 (OVERWRITTEN) -----"
            POL_Debug_LogToPrefix "----- Ending function $1 (OVERWRITTEN) -----"
        else
             # Download "function" script, check its signature, then execute it
            $POL_WGET "$SITE/V4_data/repository/get_file.php?version=playonlinux-$VERSION&id=$1" -O- > "$1"

            polMd5="$(POL_MD5_file "$1")"
            if [ "$polMd5" = "68b329da9893e34099c7d8ad5cb9c940" -o "$polMd5" = "0f1b63a74ab217a19270be0df9e0590d" ] # Empty file
            then
                POL_Debug_Error "Function $1 does not exist!"
                exitcode="1"
            else
                POL_Debug_Message "----- Starting function $1 -----"
                POL_Debug_LogToPrefix "----- Starting function $1 -----"
                POL_Source "$@"
                exitcode="$?"
                POL_Debug_Message "----- Ending function $1 -----"
                POL_Debug_LogToPrefix "----- Ending function $1 -----"
            fi
        fi
        cd "$OLD_PC_DIR"
        return "$exitcode"
    else
        POL_Debug_Warning "no function name specified"
    fi
}

POL_Source()
{
    source "$@"
}

POL_System_find_file ()
{
    # Usage: POL_System_find_file [(subdirectory/...)filename]
    # Find a file in the current virtual drive that match the provided relative path suffix, case insensitively
    # If found, returns its path relative to the drive_c subdirectory
    local suffix="$1"

    [ "$WINEPREFIX" ] || POL_Debug_Fatal "\$WINEPREFIX not set"
    pushd "$WINEPREFIX" >/dev/null || POL_Debug_Fatal "Prefix $WINEPREFIX does not exist"
    cd drive_c || POL_Debug_Fatal "drive_c folder does not exist"

    local binary_path
    if [ -e "./$suffix" ]; then
        # Explicit relative path, note that the case has to be correct
        binary_path="$suffix"
    else
        # Implicit path, search file outside of windows directory first
        local paths="$(find . -path ./windows -prune -o -ipath "*/$suffix" -a -type f -print)"
        [ -z "$paths" ] && paths="$(find ./windows -ipath "*/$suffix" -a -type f -print)"

        if [ "$(wc -l <<<"$paths")" -gt 1 ]; then
            POL_Debug_Warning "Multiple binaries found for suffix $suffix: $paths"
            # Sorting both makes result deterministic, and picks a shallow file
            binary_path="$(sort <<<"$paths"|tail -n 1)"
        else
            binary_path="$paths"
            fi
    fi
    popd > /dev/null
    echo "$binary_path"
}