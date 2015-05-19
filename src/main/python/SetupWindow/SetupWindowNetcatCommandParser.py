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

import os

from com.playonlinux.framework import EnvironmentHelper
from com.playonlinux.framework import Downloader

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
                windowTitle = "%s Wizard" % EnvironmentHelper.getApplicationName()

            self.setupWindowManager.newWindow(setupWindowId, windowTitle)

        if(self.getCommand() == "POL_SetupWindow_message"):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            self.setupWindowManager.getWindow(setupWindowId).message(textToShow)

        if(self.getCommand() == "POL_SetupWindow_wait"):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            self.setupWindowManager.getWindow(setupWindowId).wait(textToShow)

        if(self.getCommand() == "POL_SetupWindow_textbox"):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            try:
                defaultValue = self.command[4]
            except IndexError:
                defaultValue = ""

            return self.setupWindowManager.getWindow(setupWindowId).textbox(textToShow, defaultValue)

        if(self.getCommand() == "POL_SetupWindow_menu"):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            try:
                separator = self.command[5]
            except IndexError:
                separator = "~"

            items = self.command[4].split(separator)

            return self.setupWindowManager.getWindow(setupWindowId).menu(textToShow, items)

        if(self.getCommand() == "POL_SetupWindow_Close"):
            setupWindowId = self.command[2]

            self.setupWindowManager.getWindow(setupWindowId).close()

        if(self.getCommand() == "POL_Download"):
            setupWindowId = self.command[2]
            setupWindow = self.setupWindowManager.getWindow(setupWindowId)

            Downloader(setupWindow).get(self.command[3]).check(self.command[4])