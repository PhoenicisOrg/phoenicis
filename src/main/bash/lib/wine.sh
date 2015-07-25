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

POL_Wine_SelectPrefix()
{
    # Select a wineprefix and remove unexpected chars
    # Usage: POL_Wine_SelectPrefix [prefixname]
    PREFNAME=`printf "$1"| tr -c [[a-zA-Z0-9]\.] '_'`
    POL_Debug_Message "Selecting prefix: $PREFNAME"

    [ -z "$PREFNAME" ] && POL_Debug_Fatal "Bad or empty virtual drive name selected"
    export WINEPREFIX="$POL_USER_ROOT/wineprefix/$PREFNAME"
    export DOSPREFIX="$WINEPREFIX"

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
    if [ ! "$1" = "" ]; then
        POL_Debug_Message "Setting POL_WINEVERSION to $1"
        export POL_WINEVERSION="$1"
    elif [ "$POL_WINEVERSION" ]; then
        POL_Debug_Message "POL_WINEVERSION is already set to $POL_WINEVERSION. Using it"
    else
        POL_Debug_Message "No version specified. Using system version ($(wine --version))"
    fi

    POL_Debug_Message "Creating prefix ($POL_WINEVERSION)..."
    [ "$POL_ARCH" = "" ] && POL_System_SetArch "auto"
    [ "$WINEPREFIX" = "" ] && POL_Debug_Fatal "WINEPREFIX is not set!"

    if [ -e "$WINEPREFIX" ]; then
        POL_Debug_Message "Prefix already exists"
        LNG_OVERWRITE="$(eval_gettext 'Overwrite (usually works, no guarantee)')"
        LNG_ERASE="$(eval_gettext 'Erase (virtual drive content will be lost)')"
        LNG_ABORT="$(eval_gettext 'Abort installation')"

        OLD_ARCH=""
        [ -e "$WINEPREFIX/playonlinux.cfg" ] && OLD_ARCH=$(POL_Config_PrefixRead "ARCH")
        if [ "$OLD_ARCH" = "$POL_USER_ARCH" ]; then
            PREFIX_CHOICES="$LNG_OVERWRITE~$LNG_ERASE~$LNG_ABORT"
        else
            # Settings are not compatible, overwriting is not an option
            PREFIX_CHOICES="$LNG_ERASE~$LNG_ABORT"
        fi

        OLD_APP_ANWSER="$APP_ANSWER"
        PREFNAME="$(basename $WINEPREFIX)"
        POL_SetupWindow_menu "$(eval_gettext 'The target virtual drive $PREFNAME already exists:')" "$TITLE" "$PREFIX_CHOICES" "~"
        case "$APP_ANSWER" in
            "$LNG_OVERWRITE")
                # Prefix content is not reproducible, it's tempting to disallow reports
                # NOBUGREPORT="TRUE"
                POL_Debug_Message "Overwrite Prefix"
                # Should we revert what has been autodetected by SelectPrefix here too?
                ;;
            "$LNG_ERASE")
                POL_Debug_Message "Erase Prefix"
                POL_Wine_PrefixDelete
                # Revert what could have been autodetected with SelectPrefix
                POL_ARCH="$POL_USER_ARCH"
                ;;
            *)
                NOBUGREPORT="TRUE"
                POL_Debug_Fatal "$(eval_gettext 'User abort')"
                ;;
        esac
        APP_ANSWER="$OLD_APP_ANWSER"
    fi

    POL_SetupWindow_wait "$(eval_gettext 'Please wait while the virtual drive is being created...')" "$TITLE"
    if [ -e "$WINEPREFIX" ]; then
        touch "$WINEPREFIX/playonlinux.cfg"
        if [ ! "$POL_WINEVERSION" = "" ]; then
            POL_Debug_Message "Setting version to $POL_WINEVERSION"
            POL_Wine_SetVersionPrefix "$POL_WINEVERSION"
            POL_Wine_SetVersionEnv
        fi
    else    # Prefix does not exit, let's create it
        if [ "$POL_WINEVERSION" = "" ]; then
            # System wineversion
            ## Really bad idea
            ## export WINEARCH=win32
            if [ ! "$POL_ARCH" = "" ]; then
                if [ "$POL_ARCH" = "x86" ]; then
                    export WINEARCH=win32
                else
                    export WINEARCH=win64
                fi
                POL_Debug_Message "Setting WINEARCH to $WINEARCH"
            fi

            wine wineboot
            POL_Debug_InitPrefix

            if [ -e "$WINEPREFIX/drive_c/windows/syswow64" ] # It is a 64 bits prefix
            then
                POL_Config_PrefixWrite "ARCH" "amd64"
                POL_Debug_LogToPrefix "This is a 64bits prefix!"
                POL_Config_Write WINE_SYSTEM_ARCH amd64
            else
                POL_Config_PrefixWrite "ARCH" "x86"
                POL_Debug_LogToPrefix "This is a 32bits prefix!"
                POL_Config_Write WINE_SYSTEM_ARCH x86
            fi
        else
            mkdir -p "$WINEPREFIX"
            POL_Debug_Message "Using wine $POL_WINEVERSION"
            POL_Wine_InstallVersion "$POL_WINEVERSION"
            POL_SetupWindow_wait "$(eval_gettext 'Please wait while the virtual drive is being created...')" "$TITLE"
            POL_Config_PrefixWrite "ARCH" "$POL_ARCH"
            POL_Config_PrefixWrite "VERSION" "$POL_WINEVERSION"
            POL_Wine_AutoSetVersionEnv

            POL_Debug_InitPrefix

            which wineprefixcreate && [ "$(POL_MD5_file "$(which wineprefixcreate)")" != "5c0ee90682746e811698a53415b4765d" ] && [ ! "$(which wineprefixcreate | grep $APPLICATION_TITLE)" = "" ] && wine wineprefixcreate
            wine wineboot
        fi
    fi

    # Make sure that .reg files are created
    if which wineserver; then
        wineserver -w
    else
        POL_Debug_Message "Warning, wineserver not found"
        sleep 4
    fi
    POL_LoadVar_PROGRAMFILES
    [ -e "$POL_USER_ROOT/configurations/post_prefixcreate" ] && \
        source "$POL_USER_ROOT/configurations/post_prefixcreate"
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
        if [ "$LOGFILE" = "/dev/null" -o "$LOGFILE" = "" ]; then
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

detect_wineprefix()
{
	# Read the wineprefix of a shortcut
	# Usage: detect_wineprefix [Shortcut]

	local file="$POL_USER_ROOT/shortcuts/$1"
	if [ -e "$file" ]; then
		local prefix=$(grep '^export WINEPREFIX' "$file")
		prefix=${prefix:18}
		prefix=${prefix//"\""/""}
		prefix=${prefix//"//"/"/"}
		echo $prefix
	fi
}

POL_Wine_SetVersionPrefix()
{
	# Usage: POL_Wine_SetVersionPrefix [VERSION]
	# Change a prefix wine version
	[ ! "$1" = "" ] && export POL_WINEVERSION="$1"
	[ "$WINEPREFIX" = "" ] && POL_Debug_Fatal "WINEPREFIX is not set!"

	POL_Config_PrefixWrite "VERSION" "$POL_WINEVERSION"
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


POL_Wine_PrefixDelete()
{
    [ -z "$WINEPREFIX" ] && POL_Debug_Fatal "WINEPREFIX not set"
    local wineprefix="$WINEPREFIX"
    wineprefix=${wineprefix//"//"/"/"}
    local shortcuts=()
    local OLDIFS="$IFS"
    IFS=$'\n'
    cd "$POL_USER_ROOT/shortcuts/" &&
    for shortcut in *
    do
        [ "$(detect_wineprefix "$shortcut")" = "$wineprefix" ] && shortcuts+=("$shortcut")
    done
    IFS="$OLDIFS"

    # cf MainWindow.DeletePrefix()
    POL_SetupWindow_wait_next_signal "$(eval_gettext 'Uninstalling...')" "$(eval_gettext '$APPLICATION_TITLE Uninstaller')"
    if [ "${#shortcuts[@]}" -gt 0 ]; then
        POL_Debug_Warning "$wineprefix is still in use by ${#shortcuts[@]} shortcuts, removing them first"
        for shortcut in "${shortcuts[@]}"; do
            POL_Debug_Message "Removing shortcut $shortcut..."
            bash "$PLAYONLINUX/bash/uninstall" --non-interactive "$shortcut"
        done
    fi

    clean_wineprefix --non-interactive "$WINEPREFIX"
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