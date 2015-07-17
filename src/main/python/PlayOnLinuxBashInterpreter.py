#!/usr/bin/env python

# Copyright (C) 2015 Paris Quentin

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
from com.playonlinux.framework.templates import Installer


from BashBinder.BashProcess import BashProcess
from BashBinder.HealthChecher import HealthChecker
from BashBinder.NetcatServer import NetcatServer
from Environment.EnvironmentLoader import EnvironmentLoader
from SetupWindow.SetupWindowManager import SetupWindowManager

class PlayOnLinuxBashInterpreter(Installer):
    title = "PlayOnLinux bash interpreter" # FIXME

    def main(self):
        self.setupWindowManager = SetupWindowManager()
        healthChecker = HealthChecker()

        self.netcatServer = NetcatServer(self.setupWindowManager)
        self.netcatServer.initServer()
        self.netcatServer.setHealthChecker(healthChecker)

        EnvironmentLoader.setup(self.netcatServer)

        self.process = BashProcess(["bash", __scriptToWrap__])
        self.process.setHealthChecker(healthChecker)
        self.process.start()

        healthChecker.wait()
        self.netcatServer.closeServer()


    def rollback(self):
        try:
            try:
                self.process.stop()
            except AttributeError:
                pass

            try:
                self.netcatServer.closeServer()
            except AttributeError:
                pass
        finally:
            self.setupWindowManager.closeAll()
