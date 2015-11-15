#!/bin/bash

[ "$PLAYONLINUX" = "" ] && exit 0
source "$PLAYONLINUX/lib/sources"


PREFIX="JediKnightII"
TITLE="Star wars Jedi Knight II - JediOutcast"
EDITOR="LucasArts"
EDITOR_URL="http://www.lucasarts.com"
SCRIPTOR="Quentin PÂRIS"
WINEVERSION="1.4"

POL_SetupWindow_Init
POL_Debug_Init

#Presentation
POL_SetupWindow_presentation "$TITLE" "$EDITOR" "$EDITOR_URL" "$SCRIPTOR" "$PREFIX"

POL_SetupWindow_InstallMethod "CD,LOCAL"

if [ "$POL_SELECTED_FILE" ]; then
	SetupIs="$POL_SELECTED_FILE"
else
	if [ "$INSTALL_METHOD" = "CD" ]; then
		POL_SetupWindow_cdrom
		POL_SetupWindow_check_cdrom "GameData/Setup.exe"
		SetupIs="$CDROM/GameData/Setup.exe"
	fi
	if [ "$INSTALL_METHOD" = "LOCAL" ]; then
		POL_SetupWindow_browse "$(eval_gettext 'Please select the setup file to run')" "$TITLE"
		SetupIs="$APP_ANSWER"
	fi
fi

POL_Wine_SelectPrefix "$PREFIX"
POL_Wine_PrefixCreate "$WINEVERSION"

POL_Wine_WaitBefore "$TITLE"
[ "$POL_OS" = "Mac" ] && Set_Managed Off
POL_Wine "$SetupIs"

POL_Shortcut "JediOutcast.exe" "$TITLE"

POL_SetupWindow_Close
exit
-----BEGIN PGP SIGNATURE-----
Version: GnuPG v1.4.10 (GNU/Linux)

iEYEABECAAYFAk+03GAACgkQ5TH6yaoTykf1jQCfVG/941v9rfZUbYC5c2OiaPcw
N6cAnigUAexuq3+T9tZtvZTo35CvtHgk
=0R3G
-----END PGP SIGNATURE-----
