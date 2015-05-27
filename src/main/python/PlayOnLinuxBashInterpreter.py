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
import os
from BashBinder.BashProcess import BashProcess

from BashBinder.NetcatServer import NetcatServer
from Environment.EnvironmentLoader import EnvironmentLoader
from com.playonlinux.framework.templates import Script

class PlayOnLinuxBashInterpreter(Script):
    def main(self):
        setupWindowNetcatServer = NetcatServer()
        setupWindowNetcatServer.initServer()

        EnvironmentLoader.setup(setupWindowNetcatServer)

        process = BashProcess(["bash", __scriptToWrap__])
        setupWindowNetcatServer.setProcess(process)
        process.start()
        process.join()

        setupWindowNetcatServer.closeServer()


