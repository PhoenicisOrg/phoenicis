#!/bin/bash

# Copyright (C) 2007-2011 PlayOnLinux Team
# Copyright (C) 2007-2011 Pâris Quentin
# Copyright (C) 2015 - Pâris Quentin

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

# legacy.lib
# --------------
#
# This lib contains PlayOnLinux's v3 and v4 old functions, so that the scripts can stay compatible
# Please do NOT use these functions anymore !


#############################
##### Old GUI functions #####
#############################

POL_GetSetupImages ()
{
    POL_Debug_Warning "POL_GetSetupImages can no longer be used with v4 scripts"
}

POL_SetupWindow_message_image ()
{
    # Show a message with an image
    # Now, it calls POL_SetupWindow_message
    
    POL_Debug_Warning "Deprecated function POL_SetupWindow_message_image!"
    POL_SetupWindow_message "$@"
}

POL_SetupWindow_detect_exit ()
{
    # Detect if "Cancel" button has been clicked in a Setup Window. 
    # If it's the case, the function kills the scripts
    # In version 4, python automatically kills bash process, so this function has became useless
    # Usage: POL_SetupWindow_detect_exit
    
    POL_Debug_Warning "Deprecated function POL_SetupWindow_detect_exit!"
}

POL_SetupWindow_games ()
{
    # Show the list of programs installed.
    # This function has been renamed to POL_SetupWindow_shorcuts_list
    # Usage: POL_SetupWindow_shortucts_list
    
    POL_Debug_Warning "Deprecated function POL_SetupWindow_games"
    POL_SetupWindow_shortcuts_list "$@"
}

POL_SetupWindow_install_wine ()
{
    # This function installs a version of wine into playonlinux environment
    # It has been renamed to POL_Wine_InstallVersion
    # Usage: POL_SetupWindow_install_wine [VERSION]
    
    POL_Debug_Warning "Function deprecated POL_SetupWindow_install_wine"
    POL_Wine_InstallVersion "$@"
}

POL_SetupWindow_prefixcreate ()
{
    # Create a prefix (virtual drive)
    # Renamed to POL_Wine_PrefixCreate
    # Usage: POL_SetupWindow_prefixcreate
    
    POL_Debug_Warning "Very deprecated ! Use POL_Wine_PrefixCreate"
    POL_Wine_PrefixCreate "$@"
}

select_prefixe ()
{
    # Select a wineprefix
    # Usage: select_prefixe "/path/to/prefix"
    
    POL_Debug_Warning "Deprecated function ! Use POL_Wine_SelectPrefix"
    export WINEPREFIX="$1"
    export DOSPREFIX="$1" # For dosbox support
}
select_prefix ()
{
    # Copy of select_prefixe
    # Usage: select_prefix "/path/to/prefix"
    select_prefixe "$@"
}

##############################
##### Old Wine functions #####
##############################

Set_GLSL ()
{
    # Enable or disable GLSL support in the selected prefix
    # This function is regrouped with others. Therefore, you should replace it by "POL_Wine_Direct3D UseGLSL"
    # Usage: Set_GLSL (On|Off)
    POL_Debug_Warning "Set_GLSL depracted, please use POL_Wine_Direct3D UseGLSL"
    # On ou Off
    if [ "$1" = "On" ] || [ "$1" == "enabled" ]
    then
        echo "[HKEY_CURRENT_USER\Software\Wine\Direct3D]" > "$POL_USER_ROOT/tmp/glsl.reg"
        echo "\"UseGLSL\"=\"enabled\"" >> "$POL_USER_ROOT/tmp/glsl.reg"
        wine regedit "$POL_USER_ROOT/tmp/glsl.reg"
        rm -f "$POL_USER_ROOT/tmp/glsl.reg"
    fi
    if [ "$1" = "Off" ] || [ "$1" == "disabled" ]
    then
        echo "[HKEY_CURRENT_USER\Software\Wine\Direct3D]" > "$POL_USER_ROOT/tmp/glsl.reg"
        echo "\"UseGLSL\"=\"disabled\"" >> "$POL_USER_ROOT/tmp/glsl.reg"
        POL_Wine regedit "$POL_USER_ROOT/tmp/glsl.reg"
        rm -f "$POL_USER_ROOT/tmp/glsl.reg"
    fi
}
installer_wine_version ()
{
    # Copy of POL_SetupWindow_install_wine
    # Usage: POL_SetupWindow_install_wine [VERSION]
    POL_SetupWindow_install_wine "$@"
}
Set_WineVersion_Assign ()
{
    # Set a version of wine for a shortcut
    # In POL 4, wine versions are no longer assigned to a shortcut,
    # but they are assigned to a prefix.
    # This function detects the prefix of the shortcut in the first argument to assign the good version

    # Usage: Set_WineVersion_Assign [VERSION] [SHORTCUT]

    prefixold="$WINEPREFIX"
    POL_Debug_Warning "Function deprecated !"
    export WINEPREFIX="$POL_USER_ROOT/wineprefix/$(POL_Shortcut_GetPrefix "$2")"
    POL_Wine_SetVersionPrefix "$1"
    export WINEPREFIX="$prefixold"
}


fonts_to_prefixe ()
{
    # Install microsoft fonts to a prefix
    # Replaced by POL_Wine_InstallFonts
    # Usage: fonts_to_prefixe
    POL_Wine_InstallFonts "$@"
}
fonts_to_prefix ()
{
    # Install microsoft fonts to a prefix
    # Replaced by POL_Wine_InstallFonts
    # Usage: fonts_to_prefix
    POL_Wine_InstallFonts "$@"
}
Use_WineVersion()
{
    # Set the good wineversion in PATH
    # No longuer needed, use POL_WINEVERSION variable before calling POL_Wine
    # Usage: Use_WineVersion [VERSION]
    POL_Debug_Warning "Function is deprecated. Use export WINEVERSION instead"
    export POL_WINEVERSION="$1"
}




##########################################
##### Old shorcut creation functions #####
##########################################

POL_SetupWindow_make_shortcut()
{
    # Make a shortcut into PlayOnLinux
    # Use POL_Shortcut
    # Usage: POL_SetupWindow_make_shortcut [Prefix name] [Program's directory] [Program's file] [POL Website icon] [Name of the sortcut] [Leave empty] [Argument]
    
    POL_Debug_Warning "Deprecated function! Use POL_Shortcut"

    creer_lanceur_base "$@"
    if [ ! "$(POL_Config_Read NO_DESKTOP_ICON)" = "TRUE" ] 
    then
    if [ "$POL_OS" = "Mac" ] # Ouai, la c'est complètement différent sous linux et sous mac
    then
        if [ "$5" = "" ]
        then
            POL_SetupWindow_AutoApp "$1" "$1"
        else
            POL_SetupWindow_AutoApp "$5" "$1"
        fi
    
    else

        if [ -f "$POL_USER_ROOT/icones/full_size/$NOMICONE_" ]; then # L'icone existe...
            iconPath="$POL_USER_ROOT/icones/full_size/$NOMICONE_"
        else
            iconPath="$PLAYONLINUX/etc/playonlinux.png"
        fi
        make_desktop_shortcut "$iconPath" "$NOMICONE_" "$DESKTOP" "$PLAYONLINUX/playonlinux --run \"$NOMICONE_\"" "$NOMICONE_"
    fi
    fi
}

POL_SetupWindow_auto_shortcut()
{
    # Make a shortcut into PlayOnLinux, easier using than POL_SetupWindow_make_shortcut
    # Use POL_Shortcut
    # Usage: POL_SetupWindow_make_shortcut [Prefix name] [Program's binary file] [Name of the sortcut] [POL Website icon] [Argument]
    
    POL_Debug_Warning "POL_SetupWindow_auto_shortcut is deprecated"
    Binaire="$2"
    SpecialArg="$5"
    
    if [ "$3" = "" ]
    then
        NOMICONE_="$1"
    else
        NOMICONE_="$3"
    fi
    
    mkdir -p "$POL_USER_ROOT/icones/32"
    mkdir -p "$POL_USER_ROOT/icones/full_size"
    
    ICON_WEB_NAME="$4"
    ICON_OK=0

    ## On chope le dossier du binaire
    cd "$POL_USER_ROOT/wineprefix/$1" || POL_SetupError "Prefixe $1 does not exists"
    cd drive_c || POL_SetupError "drive_c folder does not exists"
    binary_path=$(find ./ -iname "$Binaire" | tail -n 1)
    binary_dir=$(dirname "$binary_path")
    Binaire=$(basename "$binary_path")
    [ "$binary_dir" = "" ] && POL_SetupError "Can't find $Binaire"

    if [ -n "$ICON_WEB_NAME" ]; then
        if [ ! "$OFFLINE" = "1" ]; then # On peut le télécharger...
            $POL_WGET "$SITE/icones/$ICON_WEB_NAME" -O- > "$POL_USER_ROOT/icones/full_size/$NOMICONE_" || rm "$POL_USER_ROOT/icones/full_size/$NOMICONE_"  # On prend l'icone full size
        fi
        
        if [ -f "$POL_USER_ROOT/icones/full_size/$NOMICONE_" ]; then
            convert -resize 32 "$POL_USER_ROOT/icones/full_size/$NOMICONE_" "$POL_USER_ROOT/icones/32/$NOMICONE_" # On fabrique l'icone 32*32
            ICON_OK=1
        fi
    elif [ "$ICON_OK" -ne 1 ]; then # Pas d'incone sur le web => Création à partir de l'exe...
        echo "No internet icon..."
        
        POL_ExtractIcon "$POL_USER_ROOT/wineprefix/$1/drive_c/$binary_dir/$Binaire" "$POL_USER_ROOT/icones/32/$NOMICONE_"
            
        # On ne crée pas d'icone "full_size", parce que les résolutions des icones windows ne sont pas suffisantes tout le temps.
        # Pour verifier la taille des icones, il faudrait utiliser "identify", mais ce programme n'est pas toujours fournis.
    fi

    ## On génère le binaire 
    echo "#!/bin/bash" > "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "[ \"\$PLAYONLINUX\" = \"\" ] && exit 0" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "source \"\$PLAYONLINUX/lib/sources\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "export WINEPREFIX=\"$POL_USER_ROOT/wineprefix/$1\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "export WINEDEBUG=\"-all\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "cd \"$POL_USER_ROOT/wineprefix/$1/drive_c/$binary_dir\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "POL_Wine \"$Binaire\" $SpecialArg \$@" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    chmod +x "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    if [ ! "$(POL_Config_Read NO_DESKTOP_ICON)" = "TRUE" ] 
    then
    if [ "$POL_OS" = "Mac" ]
    then
        POL_SetupWindow_AutoApp "$3" "$1"
    else
        if [ -f "$POL_USER_ROOT/icones/full_size/$NOMICONE_" ]; then # L'icone existe...
            iconPath="$POL_USER_ROOT/icones/full_size/$NOMICONE_"
        else
            iconPath="$PLAYONLINUX/etc/playonlinux.png"
        fi
        make_desktop_shortcut "$iconPath" "$NOMICONE_" "$DESKTOP" "$PLAYONLINUX/playonlinux --run \"$NOMICONE_\""
    fi
    fi
}
POL_SetupWindow_shortcut()
{
    # Copy of POL_Shortcut
    # Usage: POL_SetupWindow_shortcut [Program's binary file] [Name] [Icon] [Argument]

    POL_Debug_Warning "POL_SetupWindow_shortcut is deprecated"
    POL_Shortcut "$@"
}

POL_SetupError()
{
    # Old error function. Replaced by debugging API
    # Usage: command | POL_SetupError [Messagge]
    
    POL_Debug_Warning "Deprecated function"
    POL_SetupWindow_message "$1\n\nAborting." "Error"
    POL_SetupWindow_Close
    exit
}



creer_lanceur_base ()
{
    # Needed for POL_SetupWindow_make_shortcut
    # Usage: POL_SetupWindow_make_shortcut [Prefix name] [Program's directory] [Program's file] [POL Website icon] [Name of the sortcut] [Leave empty] [Argument]
    
    # 1 = Wineprefix
    # 2 = Repertoire
    # 3 = Binaire
    # 4 = Icone (png ou xpm)
    # 5 = Nom de l'icône
    # 6 = Ignoré (question de compatibilité avec les versions < 2.0)
    # 7 = Eventuel argument

    Binaire="$3"
    SpecialArg="$7"

    if [ "$5" = "" ]
    then
        NOMICONE_="$1"
    else
        NOMICONE_="$5"
    fi
    
    mkdir -p "$POL_USER_ROOT/icones/32"
    mkdir -p "$POL_USER_ROOT/icones/full_size"
    
    ICON_WEB_NAME="$4"
    ICON_OK=0

    
    # Création de l'icone.
    
    # Cas 1 : le nom de l'icone est donné.
    if [ -n "$ICON_WEB_NAME" ]; then
        if [ ! "$OFFLINE" = "1" ]; then # On peut le télécharger...
            $POL_WGET "$SITE/icones/$ICON_WEB_NAME" -O "$POL_USER_ROOT/icones/full_size/$NOMICONE_" # On prend l'icone full size
        fi
        
        if [ -f "$POL_USER_ROOT/icones/full_size/$NOMICONE_" ]; then
            convert -resize 32 "$POL_USER_ROOT/icones/full_size/$NOMICONE_" "$POL_USER_ROOT/icones/32/$NOMICONE_" # On fabrique l'icone 32*32
            ICON_OK=1
        fi
    elif [ "$ICON_OK" -ne 1 ]; then # Pas d'incone sur le web => Création à partir de l'exe...
        echo "No iternet icon..."
        
        mkdir -p "$POL_USER_ROOT/tmp/win32Icon"
        wrestool -x "$POL_USER_ROOT/wineprefix/$1/drive_c/$2/$Binaire" -o "$POL_USER_ROOT/tmp/win32Icon" -t14
        
        cd "$POL_USER_ROOT/tmp/win32Icon"
        win32IconName=$(ls -S *.ico | head -n 1)
        if [ -f "$win32IconName" ]; then # L'ico est créé
            convert ICO:"$win32IconName" -resize 32 PNG:"$POL_USER_ROOT/icones/32/$NOMICONE_" # On fabrique l'icone 32*32
        fi # Si non : icone play on linux.

        rm -r "$POL_USER_ROOT/tmp/win32Icon"
        
        # On ne crée pas d'icone "full_size", parce que les résolutions des icones windows ne sont pas suffisantes tout le temps.
        # Pour verifier la taille des icones, il faudrait utiliser "identify", mais ce programme n'est pas toujours fournis.
    fi
    
    echo "#!/bin/bash" > "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "[ \"\$PLAYONLINUX\" = \"\" ] && exit 0" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "source \"\$PLAYONLINUX/lib/sources\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "export WINEPREFIX=\"$POL_USER_ROOT/wineprefix/$1\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "export WINEDEBUG=\"-all\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "cd \"$POL_USER_ROOT/wineprefix/$1/drive_c/$2\"" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    echo "POL_Wine \"$Binaire\" $SpecialArg \$@" >> "$POL_USER_ROOT/shortcuts/$NOMICONE_"
    chmod +x "$POL_USER_ROOT/shortcuts/$NOMICONE_"
}
POL_SetupWindow_get_local_icon ()
{
    # Get a local icon (.local folder) and assign it to a shortcut
    POL_Debug_Warning "Deprecated function" 
    POL_GetLocalIcon "$@"
}

generer_icone() {
    make_desktop_shortcut "$@"
}

POL_SetupWindow_reboot ()
{
    # Simulate windows reboot
    # Usage: POL_SetupWindow_reboot
    POL_SetupWindow_wait_next_signal "$(eval_gettext 'Please wait while $APPLICATION_TITLE is simulating a Windows reboot')" "Wine"
    wine wineboot
    POL_SetupWindow_detect_exit
}


read_prefixes()
{
    # Get the list of prefixes
    cd "$POL_USER_ROOT/wineprefix"
    LIST_FILE=""
    for file in *
    do    
        if [ "$LIST_FILE" = "" ]
        then
            LIST_FILE="$file" 
        else
            LIST_FILE+="~$file"
        fi
    done
    echo $LIST_FILE
}

set_user_dir()
{
    # Change directory name's to english one
cat << EOF > /tmp/pol_user.reg
REGEDIT4

[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer]

[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Advanced]

[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders]
"AppData"="C:\\\\windows\\\\profiles\\\\$USER\\\\Application Data"
"Cache"="C:\\\\windows\\\\profiles\\\\$USER\\\\Local Settings\\\\Temporary Internet Files"
"Cookies"="C:\\\\windows\\\\profiles\\\\$USER\\\\Cookies"
"Desktop"="C:\\\\windows\\\\profiles\\\\$USER\\\\Desktop"
"Favorites"="C:\\\\windows\\\\profiles\\\\$USER\\\\Favorites"
"Fonts"="C:\\\\windows\\\\Fonts"
"History"="C:\\\\windows\\\\profiles\\\\$USER\\\\Local Settings\\\\History"
"Local AppData"="C:\\\\windows\\\\profiles\\\\$USER\\\\Local Settings\\\\Application Data"
"My Music"="C:\\\\windows\\\\profiles\\\\$USER\\\\My Music"
"My Pictures"="C:\\\\windows\\\\profiles\\\\$USER\\\\My Pictures"
"My Videos"="C:\\\\windows\\\\profiles\\\\$USER\\\\My Videos"
"NetHood"="C:\\\\windows\\\\profiles\\\\$USER\\\\Network Hood"
"Personal"="C:\\\\windows\\\\profiles\\\\$USER\\\\My documents"
"PrintHood"="C:\\\\windows\\\\profiles\\\\$USER\\\\Printing Hood"
"Programs"="C:\\\\windows\\\\profiles\\\\$USER\\\\Start Menu\\\\Programs"
"Recent"="C:\\\\windows\\\\profiles\\\\$USER\\\\Recent"
"SendTo"="C:\\\\windows\\\\profiles\\\\$USER\\\\SendTo"
"Start Menu"="C:\\\\windows\\\\profiles\\\\$USER\\\\Start Menu"
"StartUp"="C:\\\\windows\\\\profiles\\\\$USER\\\\Start Menu\\\\Programs\\\\StartUp"
"Templates"="C:\\\\windows\\\\profiles\\\\$USER\\\\Templates"

[HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders]
"AppData"="%USERPROFILE%\\\\Application Data"
"Cache"="%USERPROFILE%\\\\Local Settings\\\\Temporary Internet Files"
"Cookies"="%USERPROFILE%\\\\Cookies"
"Desktop"="%USERPROFILE%\\\\Desktop"
"Favorites"="%USERPROFILE%\\\\Favorites"
"Fonts"="C:\\\\windows\\\\Fonts"
"History"="%USERPROFILE%\\\\Local Settings\\\\Historique"
"Local AppData"="%USERPROFILE%\\\\Local Settings\\\\Application Data"
"My Music"="%USERPROFILE%\\\\My Music"
"My Pictures"="%USERPROFILE%\\\\My Pictures"
"My Videos"="%USERPROFILE%\\\\My Videos"
"NetHood"="%USERPROFILE%\\\\Network Hood"
"Personal"="%USERPROFILE%\\\\My Documents"
"PrintHood"="%USERPROFILE%\\\\Printing hood"
"Programs"="%USERPROFILE%\\\\Start Menu\\\\Programs"
"Recent"="%USERPROFILE%\\\\Recent"
"SendTo"="%USERPROFILE%\\\\SendTo"
"Start Menu"="%USERPROFILE%\\\\Start Menu"
"StartUp"="%USERPROFILE%\\\\Start Menu\\\\Programs\\\\StartUp"
"Templates"="%USERPROFILE%\\\\Templates"

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer]

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Desktop]

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Desktop\\Namespace]

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Desktop\\Namespace\\{450D8FBA-AD25-11D0-98A8-0800361B1103}]
@="My Documents"

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Desktop\\Namespace\\{645FF040-5081-101B-9F08-00AA002F954E}]
@="Trash"

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Desktop\\Namespace\\{9D20AAE8-0625-44B0-9CA7-71889C2254D9}]
@="/"

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\MyComputer]

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\MyComputer\\Namespace]

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\MyComputer\\Namespace\\{21EC2020-3AEA-1069-A2DD-08002B30309D}]
@="Control Panel"

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders]
"Common AppData"="C:\\\\windows\\\\profiles\\\\All Users\\\\Application Data"
"Common Desktop"="C:\\\\windows\\\\profiles\\\\All Users\\\\Desktop"
"Common Documents"="C:\\\\windows\\\\profiles\\\\All Users\\\\Documents"
"Common Programs"="C:\\\\windows\\\\profiles\\\\All Users\\\\Start Menu\\\\Programs"
"Common Start Menu"="C:\\\\windows\\\\profiles\\\\All Users\\\\Start Menu"
"Common StartUp"="C:\\\\windows\\\\profiles\\\\All Users\\\\Start Menu\\\\Programs\\\\StartUp"
"Common Templates"="C:\\\\windows\\\\profiles\\\\All Users\\\\Templates"
"Favorites"="C:\\\\windows\\\\profiles\\\\All Users\\\\Favorites"

[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders]
"Common AppData"="%ALLUSERSPROFILE%\\\\Application Data"
"Common Desktop"="%ALLUSERSPROFILE%\\\\Desktop"
"Common Documents"="%ALLUSERSPROFILE%\\\\Documents"
"Common Programs"="%ALLUSERSPROFILE%\\\\Start Menu\\\\Programs"
"Common Start Menu"="%ALLUSERSPROFILE%\\\\Start Menu"
"Common StartUp"="%ALLUSERSPROFILE%\\\\Start Menu\\\\Programs\\\\StartUp"
"Common Templates"="%ALLUSERSPROFILE%\\\\Templates"
"Favorites"="%ALLUSERSPROFILE%\\\\Favorites"
EOF
wine regedit /tmp/pol_user.reg
}

set_programfile_dir()
{
    # Change directory's name to english one (program file)
    #made by cendre, edited by tinou
    #email:cendrev3v3@gmail.com
    if [ ! "$WINEPREFIX" ]
    then
        export WINEPREFIX="$HOME/.wine"
    fi
    REG="$POL_USER_ROOT/tmp/reg.reg"
    echo "$(eval_gettext "Setting Program Files var")"
    rm -f "$REG"
    cat <<EOF > "$REG"
[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion]
"ProgramFilesDir"="C:\\\\Program Files"
EOF
    wine regedit "$REG"
    rm -f "$REG"
    wine wineprefixcreate
    #fi
    
    #rm -rf $REG
}

find_binary() {
    # find_binary was a private function in the first place
    POL_System_find_file "$@"
}

POL_Internal_SetXQuartzDisplay() {
    echo "Note: Ignored POL_Internal_SetXQuartzDisplay"
}