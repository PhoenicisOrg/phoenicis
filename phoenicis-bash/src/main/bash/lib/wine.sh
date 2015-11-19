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


POL_Wine_SelectPrefix()
{
    # Select a wineprefix and remove unexpected chars
    # Usage: POL_Wine_SelectPrefix [prefixname]
    PREFNAME=`printf "$1"| tr -c [[a-zA-Z0-9]\.] '_'`
    POL_Debug_Message "Selecting prefix: $PREFNAME"

    [ -z "$PREFNAME" ] && POL_Debug_Fatal "Bad or empty virtual drive name selected"
    export WINEPREFIX="$POL_USER_ROOT/wineprefix/$PREFNAME"
    export POL_WINEPREFIX="$PREFNAME"

    if [ -e "$WINEPREFIX/playonlinux.cfg" ]; then
        export POL_WINEVERSION="$(POL_Config_PrefixRead VERSION)"
        POL_System_SetArch "$(POL_Config_PrefixRead ARCH)" "detected"
    else
        touch "$WINEPREFIX/playonlinux.cfg" 2> /dev/null
    fi
}



POL_Wine_PrefixCreate()
{
    # Create a wineprefix
    # Usage: POL_Wine_PrefixCreate [VERSION]

    [ "$POL_WINEPREFIX" = "" ] && throw "Prefix is not selected"
    toPython "POL_Wine_PrefixCreate" "$POL_WINEPREFIX" "$1" "$POL_ARCH"
}



POL_Wine ()
{
    # Run the good wineversion and store the result to a logfile
    # Same usage than "wine"

    local NoErrors
    if [ "$1" = "--ignore-errors" ]; then
        NoErrors="True"
        shift
    fi

    [ "$POL_WINEPREFIX" = "" ] && export POL_WINEPREFIX="$(basename "$WINEPREFIX")"

    POL_Debug_Message "Running wine-$POL_WINEVERSION "$@" (Working directory : $PWD)"
    POL_Debug_LogToPrefix "Running wine-$POL_WINEVERSION "$@" (Working directory : $PWD)"

    local outputFifo="/tmp/$(POL_Rand)"
    local errFifo="/tmp/$(POL_Rand)"
    local env="$(export | POL_base64 | tr -d '\n\r')"
    mkfifo "$outputFifo"
    mkfifo "$errFifo"

    cat "$outputFifo" > /dev/stdout &
    cat "$errFifo" > /dev/stderr &

    exitCode="$(toPythonRet "POL_Wine" "$PWD" "$POL_WINEPREFIX" "$outputFifo" "$errFifo" "$env" "$@")"

    if [ "$errors" != 0 -a "$NoErrors" != "True" -a "$POL_IgnoreWineErrors" != "True" ]; then
        POL_Debug_Error "$(eval_gettext 'Wine seems to have crashed\n\nIf your program is running, just ignore this message')"
    fi
    POL_Debug_Message "Wine return: $exitCode"

    rm "$outputFifo"
    rm "$errFifo"

    return $exitCode
}

POL_Wine_InstallFonts()
{
    # Install microsoft fonts
    # Usage: POL_Wine_InstallFonts
    pushd .
    POL_Call POL_Install_corefonts
    popd
}



POL_Wine_WaitBefore ()
{
    # Lock bash commands until wine is exited
    # Usage : POL_Wine_WaitBefore [Program title] (--allow-kill)


    [ "$1" = "" ] && message="$(eval_gettext "Please wait...")" || message="$(eval_gettext 'Please wait while $SOFTNAME is installed...')"
    POL_SetupWindow_wait "$message" "$TITLE"

    return

    # FIXME
    if [ "$2" = "--allow-kill" ]
    then
        allowKill="true"
    else
        allowKill="false"
    fi


    if [ "$allowKill" = "true" ]
    then
        POL_SetupWindow_wait_button "$message" "$TITLE" "$(eval_gettext 'Install is done')" "wineserver -k" "$(eval_gettext 'Be careful! This will kill install process. If it is not finished, you will have to reinstall $SOFTNAME')"
    else
        POL_SetupWindow_wait "$message" "$TITLE"
    fi
    
}


POL_LoadVar_PROGRAMFILES()
{
	# Get Program Files folder name and store it to PROGRAMFILES variable
	# Usage: POL_LoadVar_PROGRAMFILES
	POL_Debug_Message "Getting Program Files name"
	[ -z "$WINEPREFIX" ] && POL_Debug_Fatal "WINEPREFIX not set"

	PROGRAMFILES=`POL_Wine cmd /c echo "%ProgramFiles%" |tr -d '\015\012'`
	if [ "${PROGRAMFILES}" = "%ProgramFiles%" ]
	then # Var is not defined by wine
		export PROGRAMFILES="Program Files"
	else
		export PROGRAMFILES="${PROGRAMFILES:3}"
	fi
}


POL_Wine_WaitExit ()
{
    # Lock bash commands until wine is exited
    # Usage : POL_Wine_WaitExit (--force-input) [Program title] (--allow-kill)

    if [ "$1" = "--force-input" ]
    then
        forceInput="true"
        shift
    else
        forceInput="false"
    fi

    [ "$1" = "" ] && message="$(eval_gettext "Please wait...")" || message="$(eval_gettext 'Please wait while $SOFTNAME is installed...')"



    POL_SetupWindow_wait "$message" "$TITLE"
    [ "$forceInput" = "true" ] && POL_SetupWindow_message "$(eval_gettext 'Press next only when the installation process is finished')" "$TITLE"

    return
    # FIXME


    SOFTNAME="$1"
    if [ "$2" = "--allow-kill" ] 
    then
        allowKill="true"
    else
        allowKill="false"
    fi
    
    if [ "$allowKill" = "true" ]
    then
        POL_SetupWindow_wait_button "$message" "$TITLE" "$(eval_gettext 'Install is done')" "wineserver -k" "$(eval_gettext 'Be careful! This will kill install process. If it is not finished, you will have to reinstall $SOFTNAME')"
    else
        POL_SetupWindow_wait "$message" "$TITLE"
    fi
    wineserver -w || POL_SetupWindow_message "$(eval_gettext 'Press next only when the installation process is finished')" "$TITLE"
    [ "$forceInput" = "true" ] && POL_SetupWindow_message "$(eval_gettext 'Press next only when the installation process is finished')" "$TITLE"
}

POL_Wine_OverrideDLL()
{
	# Override DLLs
	MODE=$1
	[ "$MODE" = "disabled" ] && unset MODE
	shift

	(cat << 'EOF'
REGEDIT4

[HKEY_CURRENT_USER\Software\Wine\DllOverrides]
EOF

	for DLL in "$@"
	do
		echo "\"*$DLL\"=\"$MODE\""
	done) > "$POL_USER_ROOT/tmp/override-dll.reg"

	POL_Debug_Message "Overriding DLLs"
	POL_SetupWindow_wait_next_signal "$(eval_gettext 'Please wait...')" "$TITLE"
	POL_Wine regedit "$POL_USER_ROOT/tmp/override-dll.reg"
	rm "$POL_USER_ROOT/tmp/override-dll.reg"
}

POL_Wine_DelOverrideDLL()
{
	# Delete override DLLs
	(cat << 'EOF'
REGEDIT4

[HKEY_CURRENT_USER\Software\Wine\DllOverrides]
EOF

	for DLL in "$@"
	do
		echo "\"$DLL\"=-"
		echo "\"*$DLL\"=-"
	done) > "$POL_USER_ROOT/tmp/del-override-dll.reg"
	POL_Debug_Message "Deleting overrides DLLs"
	POL_SetupWindow_wait_next_signal "Please wait" "$TITLE"
	POL_Wine regedit "$POL_USER_ROOT/tmp/del-override-dll.reg"
	rm "$POL_USER_ROOT/tmp/del-override-dll.reg"
}
POL_Wine_OverrideDLL_App()
{
	# App-specific DLL overrides
	APP="$1"
	MODE="$2"
	[ "$MODE" = "disabled" ] && unset MODE
	shift 2

	(cat << EOF
REGEDIT4

[HKEY_CURRENT_USER\\Software\\Wine\\AppDefaults\\$APP\\DllOverrides]
EOF
	for DLL in "$@"
	do
		# Who knows what this hack is for?
		if [ "$DLL" = "comctl32" ]; then
			rm -rf "$WINEPREFIX/winsxs/manifests/x86_microsoft.windows.common-controls_6595b64144ccf1df_6.0.2600.2982_none_deadbeef.manifest"
		fi
		echo "\"*$DLL\"=\"$MODE\""
	done) > "$POL_USER_ROOT/tmp/app-dll-override.reg"
	POL_Wine regedit "$POL_USER_ROOT/tmp/app-dll-override.reg"
	rm "$POL_USER_ROOT/tmp/app-dll-override.reg"
}
POL_Wine_DelOverrideDLL_App()
{
	# Delete app-specific DLL overrides
	APP="$1"
	shift

	(cat << EOF
REGEDIT4

[HKEY_CURRENT_USER\\Software\\Wine\\AppDefaults\\$APP\\DllOverrides]
EOF
	for DLL in "$@"
	do
		echo "\"$DLL\"=-"
		echo "\"*$DLL\"=-"
	done) > "$POL_USER_ROOT/tmp/del-app-dll-override.reg"
	POL_Wine regedit "$POL_USER_ROOT/tmp/del-app-dll-override.reg"
	rm "$POL_USER_ROOT/tmp/del-app-dll-override.reg"
}

POL_Wine_InstallVersion()
{
	# Install a wineversion
	# Usage: POL_Wine_InstallVersion [VERSION]

	[ ! "$1" = "" ] && export POL_WINEVERSION="$1"
	[ "$POL_WINEVERSION" = "" ] && POL_Debug_Fatal "No POL_WINEVERSION set"
	[ "$POL_ARCH" = "" ] && POL_System_SetArch "auto"
	POL_Debug_Message "Installing wine version path: $POL_WINEVERSION, $POL_ARCH"

	toPython "POL_Wine_InstallVersion" "$POL_WINEVERSION" "$POL_ARCH"
}