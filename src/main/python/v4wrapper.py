#!/usr/bin/env python

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


# This tools is made to run legacy PlayOnLinux v4 com.playonlinux.scripts
import os
import subprocess

from SetupWindow.SetupWindowNetcatServer import SetupWindowNetcatServer
from com.playonlinux.framework import EnvironementHelper

if __name__ == '__main__':
    setupWindowNetcatServer = SetupWindowNetcatServer()
    setupWindowNetcatServer.initServer()

    os.environ["PLAYONLINUX"] = os.path.join(os.path.dirname(__file__), "..", "bash")
    os.environ["POL_PORT"] = str(setupWindowNetcatServer.getPort())
    os.environ["POL_COOKIE"] = setupWindowNetcatServer.getCookie()
    os.environ["POL_OS"] = EnvironementHelper.getOperatinSystem().fetchShortName()

    process = subprocess.call(["bash", __scriptToWrap__])

    setupWindowNetcatServer.closeServer()


