#!/bin/env bash

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



POL_Shortcut()
{
	# Make a shortcut
	# Usage: POL_Shortcut [binary] [shortcut name] [playonlinux website icon] [argument] [categories]
	# If playonlinux website icon is not specified, playonlinux will try to extract it from the program
	# Possibly to use a path instead of the executable.

	BINARY="$1"
	SpecialArg="$4"
	Categories="$5"
	if [ "$2" = "" ]
	then
		ICON_FILENAME="$1"
	else
		ICON_FILENAME="$2"
	fi

	mkdir -p "$POL_USER_ROOT/icones/32"
	mkdir -p "$POL_USER_ROOT/icones/full_size"

	ICON_WEB_NAME="$3"
	ICON_OK=0

	local binary_path="$(POL_System_find_file "$BINARY")"
	local binary_dir="$(dirname "$binary_path")"
	local binary_name="$(basename "$binary_path")"

	[ -z "$binary_dir" ] && POL_Debug_Fatal "Can't find $BINARY"
	POL_Debug_Message "Looking for <${BINARY}>, found <${binary_path}>"

	local target="$WINEPREFIX/drive_c/$binary_dir/$binary_name"
	[[ "$target" =~ \.lnk$ ]] && target="$(winepath -u "$(strings "$target"|tail -n 1)")"

	if [ -n "$ICON_WEB_NAME" ]; then
		if [ ! "$OFFLINE" = "1" ]; then # It can be downloaded...
			$POL_WGET "$SITE/icones/$ICON_WEB_NAME" -O- > "$POL_USER_ROOT/icones/full_size/$ICON_FILENAME" || rm "$POL_USER_ROOT/icones/full_size/$ICON_FILENAME"  # Take the full sized icon
		fi

		if [ -f "$POL_USER_ROOT/icones/full_size/$ICON_FILENAME" ]; then
			convert -resize 32 "$POL_USER_ROOT/icones/full_size/$ICON_FILENAME" "$POL_USER_ROOT/icones/32/$ICON_FILENAME" # Scale it down to 32*32
			ICON_OK=1
		fi
	elif [ "$ICON_OK" -ne 1 ]; then # No icon from the web, make one off the exe
		POL_ExtractIcon "$target" "$POL_USER_ROOT/icones/32/$ICON_FILENAME"
		POL_ExtractBiggestIcon "$target" "$POL_USER_ROOT/icones/full_size/$ICON_FILENAME"
	fi

	if [ -z "$binary_name" ]; then
		POL_Debug_Error "$(eval_gettext 'Binary not found: $BINARY\nHave you installed the program to the default location?')"
	else
		## We generate shortcut
		(echo "#!/usr/bin/env playonlinux-bash"
		echo "[ \"\$PLAYONLINUX\" = \"\" ] && exit 0"
		echo "source \"\$PLAYONLINUX/lib/sources\""
		echo "export WINEPREFIX=\"$WINEPREFIX\""
		echo "export WINEDEBUG=\"-all\""
		[ -n "$LOGTITLE" ] && echo "#POL_Log=$LOGTITLE"
		[ -n "$SCRIPTID" ] && echo "#ScriptID=$SCRIPTID"
		if [[ "$binary_name" =~ \.(lnk|bat|cmd)$ ]]; then
			echo "cd \"$(dirname "$target")\""
			echo "POL_Wine start.exe /wait /unix \"\$WINEPREFIX/drive_c/$binary_dir/$binary_name\" $SpecialArg \"\$@\""
		else
			echo "cd \"$WINEPREFIX/drive_c/$binary_dir\""
			echo "POL_Wine \"$binary_name\" $SpecialArg \"\$@\""
		fi) > "$POL_USER_ROOT/shortcuts/$ICON_FILENAME"
		chmod +x "$POL_USER_ROOT/shortcuts/$ICON_FILENAME"

		if [ -f "$POL_USER_ROOT/icones/full_size/$ICON_FILENAME" ]; then
			iconPath="$POL_USER_ROOT/icones/full_size/$ICON_FILENAME"
		else
			# Default icon
			iconPath="$PLAYONLINUX/etc/playonlinux.png"
		fi

		# Menus
		if [ ! "$(POL_Config_Read NO_MENU_ICON)" = "TRUE" ]; then
			# Do nothing on Mac OS
			if [ -n "$Categories" -a "$POL_OS" = "Linux" ] || [ -n "$Categories" -a "$POL_OS" = "BSD" ]; then
				LOCALAPPS="$HOME/.local/share/applications"
				POL_Download_Resource "$iconPath" "$ICON_FILENAME" "$LOCALAPPS" "$PLAYONLINUX/playonlinux --run \"$ICON_FILENAME\"" "$binary_name" "$Categories" "playonlinux-"
			fi
		fi

		# Desktop
		if [ ! "$(POL_Config_Read NO_DESKTOP_ICON)" = "TRUE" ]; then
			if [ "$POL_OS" = "Mac" ]; then
				POL_SetupWindow_AutoApp "$ICON_FILENAME"
			else

				make_desktop_shortcut "$iconPath" "$ICON_FILENAME" "$DESKTOP" "$PLAYONLINUX/playonlinux --run \"$ICON_FILENAME\"" "$binary_name" "$Categories"
			fi
		fi
	fi
	POL_Debug_Message "Shortcut created: $binary_name $ICON_FILENAME"
}