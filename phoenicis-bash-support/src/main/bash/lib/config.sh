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



POL_Config_PrefixRead ()
{
    # Read an entry in wine current prefix's config file
    # Usage : POL_Config_PrefixRead [KEY]

    [ "$POL_WINEPREFIX" = "" ] && export POL_WINEPREFIX="$(basename "$WINEPREFIX")"

    toPythonRet "POL_Config_PrefixRead" "$POL_WINEPREFIX" "$1"
}

POL_Config_PrefixWrite ()
{
	# Write something to current wine prefix's config file
	# Usage : POL_Config_PrefixWrite [KEY] [VALUE]

    [ "$POL_WINEPREFIX" = "" ] && export POL_WINEPREFIX="$(basename "$WINEPREFIX")"

	toPythonRet "POL_Config_PrefixWrite" "$POL_WINEPREFIX" "$1" "$2"
	POL_Debug_Message "Prefix config write: $POL_WINEPREFIX $1 $2"
}