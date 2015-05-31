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

export POL_HOST="127.0.0.1"

POL_EscapeTab()
{
    echo "${1//	/\\t}"
}

# Silent netcat
ncs()
{
    ncns "$@" > /dev/null 2> /dev/null
}

ncns()
{
    if [ "$POL_OS" = "MACOSX" ]; then
        nc "$@"
    else
        nc -q -1 "$@" 2> /dev/null || nc "$@"
    fi

}

toPythonPipe() {
    ncs "$POL_HOST" "$POL_PORT"
}

toPython() {
    local command="$1"
    shift
    arguments=""
    for argument in "$@"
    do
        arguments="${arguments}	$(POL_EscapeTab "$argument")"
    done

    local arguments="$2"

    echo "$POL_COOKIE	$command	$$	$arguments" | toPythonPipe
}
