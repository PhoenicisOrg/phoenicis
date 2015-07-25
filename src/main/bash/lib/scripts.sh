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

POL_SetupWindow_download() {
	# Download a file and place it to the current directory
	# Usage: POL_SetupWindow_download [message] [title] [url] [file]
	# /!\ Scriptors should directly use POL_Download
        # If provided, make sure the filename is absolute to avoid any misinterpretation from server

    [ "$4" = "" ] && file="$PWD" || file="$4"
    toPython "POL_Download" "$3" "$file"
}

POL_Download_GetSize()
{
	# Get the size of a distant file
	# Usage: POL_Download_GetSize [URL]

	wget "$1" --spider --server-response -O - 2>&1 | $SED -ne '/Content-Length/{s/.*: //;p}'	| tail -n1
}

POL_Download_Resource ()
{
	# Download a file and place it in the resource directory (if it does not exist)
	# Usage: POL_Download_Resource [URL] [MD5] (Folder)

	POL_Debug_Message "Downloading resource $1"
	local URL="$1"
	local SERVER_MD5="$2"
	local SUBFOLDER="$3"
	local ATTEMPT
	[ "$4" ] && ATTEMPT="$4" || ATTEMPT=1
	local FILE="${URL##*/}"
	mkdir -p "$POL_USER_ROOT/ressources/$SUBFOLDER"
	cd "$POL_USER_ROOT/ressources/$SUBFOLDER" || POL_Debug_Fatal "Resource subfolder $SUBFOLDER does not exist"
	POL_Debug_Message "Resource name: $FILE"
	if [ -e "$FILE" ] && [ "$(POL_MD5_file "$FILE")" = "$SERVER_MD5" ]
	then
		POL_Debug_Message "Resource already present"
	else
		local WTCACHE=""
		[ -d "$HOME/.cache/winetricks" ] && WTCACHE="$(find $HOME/.cache/winetricks -type f -a -iname "$FILE" | tail -n 1)"
		if [ -n "$WTCACHE" -a "$(POL_MD5_file "$WTCACHE")" = "$SERVER_MD5" ]
		then
			ln "$WTCACHE" "$FILE" || cp "$WTCACHE" "$FILE"
			POL_Debug_Message "Resource found in winetricks cache"
		elif [ "$URL" = "" ]
		then
			POL_Debug_Error "URL is missing !"
		else

			local neededSpace="$(POL_Download_GetSize "$URL")"
			if [ "$neededSpace" ]; then
				let neededSpace=neededSpace/1024
				POL_System_EnoughSpace "$neededSpace" || POL_Debug_Error "$(eval_gettext 'No enough space to download:\n$URL ($neededSpace KB)')"
			fi
			[ -e "$FILE" ] && rm "$FILE"
			echo "Here"
			POL_SetupWindow_download "Please wait while $APPLICATION_TITLE is downloading: $FILE" "$TITLE" "$URL"
            echo "Here2"


			if [ "$Result" = "Fail" ]; then
				local APP_ANSWER
				POL_SetupWindow_question "$URL\n\n$(eval_gettext 'An error happened during download.')\n\n$(eval_gettext 'Do you want to retry?')"
				if [ "$APP_ANSWER" = "FALSE" ]; then
					POL_Debug_Error "error during download! ($ATTEMPT attempt)"
				else
					POL_Download_Resource "$URL" "$SERVER_MD5" "$SUBFOLDER" "$(( ATTEMPT + 1))"
				fi
			elif [ "$SERVER_MD5" = "" ]
			then
				POL_Debug_Warning "MD5 is missing !"
			else
				local LOCAL_MD5="$(POL_MD5_file "$FILE")"
				if [ "$LOCAL_MD5" = "$SERVER_MD5" ]
				then
					POL_Debug_Message "Download MD5 matches"
				else
					local APP_ANSWER
					POL_SetupWindow_question "$URL\n\n$(eval_gettext 'Error ! Files mismatch\n\nLocal : $LOCAL_MD5\nServer : $SERVER_MD5')\n\n$(eval_gettext 'Do you want to retry?')"
					if [ "$APP_ANSWER" = "FALSE" ]; then
						POL_Debug_Error "MD5 sum mismatch ! ($ATTEMPT attempt)"
					else
						POL_Download_Resource "$URL" "$SERVER_MD5" "$SUBFOLDER" "$(( ATTEMPT + 1 ))"
					fi
				fi
			fi
		fi
	fi
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