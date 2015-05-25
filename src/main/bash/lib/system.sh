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