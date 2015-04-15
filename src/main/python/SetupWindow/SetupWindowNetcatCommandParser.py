#!/usr/bin/env python

import os

from SetupWindowManager import SetupWindowManager
from scripts import EnvironementHelper

class SetupWindowNetcatCommandParser(object):
    def __init__(self, setupWindowManager, command):
        self.command = command.split("\t")
        self.setupWindowManager = setupWindowManager

    def getCookie(self):
        return self.command[0]

    def getCommand(self):
        return self.command[1]

    def executeCommand(self):
        if(self.getCommand() == "POL_SetupWindow_Init"):
            setupWindowId = self.command[2]
            if("TITLE" in os.environ.keys()):
                windowTitle = os.environ["TITLE"]
            else:
                windowTitle = "%s Wizard" % EnvironementHelper.getApplicationName()
            self.setupWindowManager.newWindow(setupWindowId, windowTitle)

        if(self.getCommand() == "POL_SetupWindow_message"):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            self.setupWindowManager.getWindow(setupWindowId).message(textToShow)

        if(self.getCommand() == "POL_SetupWindow_Close"):
            setupWindowId = self.command[2]

            self.setupWindowManager.getWindow(setupWindowId).close()