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



POL_Wine_SetVersionEnv()
{
	# Usage: POL_Wine_SetWineVersion [VERSION]
	# Get first argument's wine version and set PATH environment variable
	[ ! "$1" = "" ] && export POL_WINEVERSION="$1"
	[ "$POL_WINEVERSION" = "" ] && POL_Debug_Warning "No POL_WINEVERSION set, assuming it is reset"
	[ "$POL_WINEVERSION" = "" ] && export POL_WINEVERSION="--reset"
	if [ ! "$POL_WINEVERSION" = "" ]
	then
		[ "$POL_ARCH" = "" ] && POL_System_SetArch "auto"

		#POL_Debug_Message "Setting wine version path: $POL_WINEVERSION, $POL_ARCH"
		[ "$POL_OS" = "Mac" ] && ARCH_PREFIX="darwin"
		[ "$POL_OS" = "FreeBSD" ] && ARCH_PREFIX="freebsd"
		[ "$POL_OS" = "Linux" ] && ARCH_PREFIX="linux"
		OLDPATH="$PWD"
		WINEDIR="$POL_USER_ROOT/wine/$ARCH_PREFIX-$POL_ARCH"
		mkdir -p "$WINEDIR"
		cd "$WINEDIR"

		if [ "$POL_WINEVERSION" = "--reset" ]
		then
			export PATH="$PATH_ORIGIN"
			export LD_LIBRARY_PATH="$LD_PATH_ORIGIN"
			[ "$POL_OS" = "FreeBSD" ] && export LD_32_LIBRARY_PATH="$LD_32_PATH_ORIGIN"
			export POL_WINEVERSION=""
		else
			if [ ! -e "$WINEDIR/$POL_WINEVERSION" ]
			then
				POL_Debug_Message "$WINEDIR/$POL_WINEVERSION does not exist"
				POL_Debug_Message "Wine $POL_WINEVERSION not installed. Installing it"
		 		POL_Wine_InstallVersion "$POL_WINEVERSION"
			fi
			export PATH="$WINEDIR/$POL_WINEVERSION/bin/:$PATH"
			export LD_LIBRARY_PATH="$WINEDIR/$POL_WINEVERSION/lib/:$WINEDIR/$POL_WINEVERSION/lib64/:$LD_LIBRARY_PATH"
			[ "$POL_OS" = "FreeBSD" ] && export LD_32_LIBRARY_PATH="$WINEDIR/$POL_WINEVERSION/lib/:$LD_32_LIBRARY_PATH"
		fi
	fi
	cd "$OLDPATH"
}

POL_Wine_AutoSetVersionEnv()
{
	# Get the current prefix's version and set PATH environment variable
	# Usage: POL_Wine_AutoSetVersionEnv
	[ "$WINEPREFIX" = "" ] && POL_Debug_Fatal "WINEPREFIX is not set!"
	POL_WINEVERSION="$(POL_Config_PrefixRead "VERSION")"
	POL_ARCH="$(POL_Config_PrefixRead "ARCH")"
	[ "$POL_WINEVERSION" = "" ] || POL_Wine_SetVersionEnv
}

POL_Wine ()
{
	POL_Internal_SetXQuartzDisplay
	# Run the good wineversion and store the result to a logfile
	# Same usage than "wine"
	mkdir -p "$WINEPREFIX"
	touch "$WINEPREFIX/playonlinux.log"
	local NoErrors
	if [ "$1" = "--ignore-errors" ]; then
		NoErrors="True"
		shift
	fi

	POL_Wine_AutoSetVersionEnv
	POL_Debug_Message "Running wine-$POL_WINEVERSION "$@" (Working directory : $PWD)"
	POL_Debug_LogToPrefix "Running wine-$POL_WINEVERSION "$@" (Working directory : $PWD)"

        # Either that or monitor "err:process:create_process starting 64-bit process L"xxx" not supported in 32-bit wineprefix\nwine: Bad EXE format for xxx." in logs
        if [ "$POL_ARCH" = "x86" -a -e "$1" ]; then
            local EXEFILE="$1"
            if POL_System_is64bit "$EXEFILE"; then
                NOBUGREPORT="TRUE" # user mistake
                POL_Debug_Fatal "$(eval_gettext 'Starting 64-bit process $EXEFILE is not supported in 32-bit virtual drives')"
            fi
        fi

        if [ ! "$WINEMENUBUILDER_ALERT" ]; then
		POL_Debug_Message "Notice: PlayOnLinux deliberately disables winemenubuilder. See http://www.playonlinux.com/fr/page-26-Winemenubuilder.html"
		WINEMENUBUILDER_ALERT="Done"
	fi
	if [ "$1" = "regedit" -a ! "$2" = "" ]; then
		if [ -e "$2" ]; then
			POL_Debug_LogToPrefix "Content of $2"
			(echo '-----------'
			 cat "$2"
			 echo '-----------') >> "$WINEPREFIX/playonlinux.log"
		else
			POL_Debug_LogToPrefix "regedit parameter '$2' is not a file, not dumped to log"
		fi
	elif [ "$1" = "regedit" ]; then
		POL_Debug_LogToPrefix "User modified something in the registry manually"
	fi



	if [ "$POL_OS" = "Linux" ] || [ "$POL_OS" = "Mac" ];
	then
		if [ "$LOGFILE" = "/dev/null" ]; then
			$BEFORE_WINE $(POL_Config_Read BEFORE_WINE) wine "$@"  2> >(grep -v menubuilder --line-buffered | tee -a "$WINEPREFIX/playonlinux.log" >&2) > >(tee -a "$WINEPREFIX/playonlinux.log")
			errors=$?
		else
			$BEFORE_WINE $(POL_Config_Read BEFORE_WINE) wine "$@" 2> >(grep -v menubuilder --line-buffered | tee -a "$LOGFILE" "$WINEPREFIX/playonlinux.log" >&2) > >(tee -a "$LOGFILE" "$WINEPREFIX/playonlinux.log")
			errors=$?
		fi
	else
		# FIXME
		$BEFORE_WINE $(POL_Config_Read BEFORE_WINE) wine "$@"  2> "$WINEPREFIX/playonlinux.log" > "$WINEPREFIX/playonlinux.log"
		errors=$?
	fi

	if [ "$errors" != 0 -a "$NoErrors" != "True" -a "$POL_IgnoreWineErrors" != "True" ]; then
		POL_Debug_Error "$(eval_gettext 'Wine seems to have crashed\n\nIf your program is running, just ignore this message')"
	fi
	POL_Debug_Message "Wine return: $errors"
	return $errors
}
