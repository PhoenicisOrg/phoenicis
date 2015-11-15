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



POL_MD5 ()
{
	# Give the md5 sum of a string
	# Usage: POL_MD5 <string>

	if [ "$POL_OS" == "Mac" ] || [ "$POL_OS" == "FreeBSD" ]
	then
		printf "$1" | md5
	fi
	if [ "$POL_OS" == "Linux" ]
	then
		printf "$1" | md5sum | awk '{print $1}'
	fi
}
POL_MD5_file ()
{
	# Give the md5 sum of a file
	# Usage: POL_MD5_file <file>

	if [ "$POL_OS" == "Mac" ] || [ "$POL_OS" == "FreeBSD" ]
	then
		cat "$1" | md5
	fi
	if [ "$POL_OS" == "Linux" ]
	then
		cat "$1" | md5sum | awk '{print $1}'
	fi
}
POL_base64()
{
	[ "$POL_OS" = "Linux" ] && base64
	[ "$POL_OS" = "Mac" ] || [ "$POL_OS" == "FreeBSD" ] && openssl base64
}
POL_unbase64()
{
	[ "$POL_OS" = "Linux" ] && base64 -d
	[ "$POL_OS" = "Mac" ] || [ "$POL_OS" == "FreeBSD" ] && openssl base64 -d
}

POL_System_unzip ()
{
	POL_Debug_Message "Starting unzip $@"
	unzip "$@" || POL_Debug_Fatal "POL_System_unzip failed with error $?!"
	POL_Debug_Message "unzip ok"
}
POL_System_7z ()
{
	POL_Debug_Message "Starting 7z $@"
	7z "$@" || POL_Debug_Fatal "POL_System_7z failed with error $?!"
	POL_Debug_Message "7z ok"
}
POL_System_tar ()
{
	POL_Debug_Message "Starting tar $@"
	tar "$@" || POL_Debug_Fatal "POL_System_tar failed with error $?!"
	POL_Debug_Message "tar ok"
}
POL_System_cabextract ()
{
	POL_Debug_Message "Starting cabextract $@"
	cabextract "$@" || POL_Debug_Fatal "POL_System_cabextract failed with error $?!"
	POL_Debug_Message "cabextract ok"
}
POL_System_unrar ()
{
	POL_Debug_Message "Starting unrar $@"
	unrar "$@" || POL_Debug_Fatal "POL_System_unrar failed with error $?!"
	POL_Debug_Message "unrar ok"
}