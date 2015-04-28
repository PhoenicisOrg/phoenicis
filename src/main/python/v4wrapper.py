#!/usr/bin/env python

# This tools is made to run legacy PlayOnLinux v4 com.playonlinux.scripts
import os
import subprocess

from SetupWindow.SetupWindowNetcatServer import SetupWindowNetcatServer
from com.playonlinux.scripts import EnvironementHelper

if __name__ == '__main__':
    setupWindowNetcatServer = SetupWindowNetcatServer()
    setupWindowNetcatServer.initServer()

    os.environ["PLAYONLINUX"] = os.path.join(os.path.dirname(__file__), "..", "bash")
    os.environ["POL_PORT"] = str(setupWindowNetcatServer.getPort())
    os.environ["POL_COOKIE"] = setupWindowNetcatServer.getCookie()
    os.environ["POL_OS"] = EnvironementHelper.getOperatinSystem().fetchShortName()

    process = subprocess.call(["bash", __scriptToWrap__])

    setupWindowNetcatServer.closeServer()


