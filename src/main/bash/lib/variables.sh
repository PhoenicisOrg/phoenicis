#!/bin/bash

# Copyright (C) 2007-2010 PlayOnLinux Team
# Copyright (C) 2011 Pâris Quentin
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

## 
# This is just a copy of function in playonlinux, but we need it earlier
[ "$POL_OS" = "Mac" ] && export LANG="$RLANG"
POL_Config_pRead (){ cat "$POL_USER_ROOT/playonlinux.cfg" 2> /dev/null | grep "^$1=" | cut -d\= -f2-; }

### Follow symlinks for find
alias find="find -H"

### Security feature
[ "$(which sudo)" ] && sudo -k 2> /dev/null

### Terminal
# User override, must support -T to set title and -e to run a command
# gnome-terminal doesn't support -T, use gnome-terminal.wrapper instead
POL_TERM="$(POL_Config_pRead POL_TERM)"

if [ -z "$POL_TERM" ] || ! type -p "$POL_TERM" >&/dev/null; then
    # Debian
    if type -p x-terminal-emulator >&/dev/null; then
		POL_TERM="x-terminal-emulator"
	fi
    # Add support for more distros or desktop specific mechanisms here
fi

# Fallback
if [ -z "$POL_TERM" ] || ! type -p "$POL_TERM" >&/dev/null; then
    POL_TERM="xterm"
fi
export POL_TERM

### Desktop
[ "$(which xdg-user-dir)" ] && export DESKTOP="$(xdg-user-dir DESKTOP)" || export DESKTOP="$HOME/Desktop"

### Override ~/.wgetrc
export WGETRC="$POL_USER_ROOT/configurations/wgetrc"

### Language
eval_gettext() { printf "$@"; }

which shasum > /dev/null 2> /dev/null || shasum () { sha1sum "$@"; }


if [ "$POL_OS" == "Linux" ] 
then 
    export POL_LANG="$(echo "$LANG" | cut -d\_ -f 1)"
    export MD5_COMMAND="md5sum"
    export SED="sed"
fi

if [ "$POL_OS" == "Mac" ]
then
    export POL_LANG="$(echo "$RLANG" | cut -d _ -f1)"
    export MD5_COMMAND="md5"
    export SED="sed"
fi
	
if [ "$POL_OS" == "FreeBSD" ] 
then 
    export POL_LANG="$(echo "$LANG" | cut -d\_ -f 1)"
    export MD5_COMMAND="md5"
    export SED="gsed"
fi

# Exit codes
EXIT_ERROR=255
EXIT_SUCCES=0

# Default architecture
export POL_USER_ARCH="x86"
export POL_ARCH="$POL_USER_ARCH"

# Miscellaneous stuff
export IGNORE_ICON_DIR="false"
export GNUPGHOME="$REPERTOIRE/gpg"

export SITE="http://repository.playonlinux.com"

# Drivers issue
[ "$(POL_Config_pRead FIX_DRI_PATH)" = "TRUE" ] && [ "$POL_OS" = "Linux" ] && export LIBGL_DRIVERS_PATH="/usr/lib/dri/:/usr/lib64/dri/:/usr/lib32/dri/"
unset WINEARCH


if [ "$POL_OS" = "Mac" ]; then
    POL_WGET="wget --prefer-family=IPv4 -q"
else
    if [ -e "/proc/net/if_inet6" ]; then
        POL_WGET="env LD_LIBRARY_PATH=$LD_LIBRARY_PATH wget -q"
    else
        POL_WGET="env LD_LIBRARY_PATH=$LD_LIBRARY_PATH wget --prefer-family=IPv4 -q"
    fi
fi

export REPERTOIRE="$POL_USER_ROOT"