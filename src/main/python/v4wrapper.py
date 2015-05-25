#!/usr/bin/env python
# coding=utf-8

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


# This tools is made to run legacy PlayOnLinux v4 scripts
import subprocess

from SetupWindow.SetupWindowNetcatServer import SetupWindowNetcatServer
from Environment.EnvironmentLoader import EnvironmentLoader

if __name__ == '__main__':
    setupWindowNetcatServer = SetupWindowNetcatServer()
    setupWindowNetcatServer.initServer()

    EnvironmentLoader.setup(setupWindowNetcatServer)

    print("Running %s" % __scriptToWrap__) # FIXME: Need a logger here
    process = subprocess.call(["bash", __scriptToWrap__])

    setupWindowNetcatServer.closeServer()


