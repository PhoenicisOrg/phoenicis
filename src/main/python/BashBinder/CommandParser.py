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

from com.playonlinux.framework import Downloader
from com.playonlinux.framework import ScriptFailureException

from java.io import File


class CommandParser(object):
    def __init__(self, setupWindowManager, command):
        self.command = command
        self.splitCommand = self.command.split("\t")
        self.setupWindowManager = setupWindowManager

    def getCookie(self):
        return self.splitCommand[0]

    def getCommand(self):
        return self.splitCommand[1]

    def executeCommand(self):
        commandExecutor = CommandParser.CommandExecutor(self.splitCommand, self.setupWindowManager)
        return getattr(commandExecutor, self.getCommand())()

    class CommandExecutor():
        def __init__(self, command, setupWindowManager):
            self.command = command
            self.setupWindowManager = setupWindowManager

        def POL_SetupWindow_Init(self):
            setupWindowId = self.command[2]
            if("TITLE" in os.environ.keys()):
                windowTitle = os.environ["TITLE"]
            else:
                windowTitle = "${application.name} Wizard";

            self.setupWindowManager.newWindow(setupWindowId, windowTitle)

        def POL_SetupWindow_message(self):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            self.setupWindowManager.getWindow(setupWindowId).message(textToShow)
        
        def POL_SetupWindow_free_presentation(self):
            setupWindowId = self.command[2]
            textToShow = self.command[3]
            
            self.setupWindowManager.getWindow(setupWindowId).presentation(textToShow)

        def POL_SetupWindow_wait(self):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            self.setupWindowManager.getWindow(setupWindowId).wait(textToShow)

        def POL_SetupWindow_textbox(self):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            try:
                defaultValue = self.command[4]
            except IndexError:
                defaultValue = ""

            return self.setupWindowManager.getWindow(setupWindowId).textbox(textToShow, defaultValue)

        def POL_SetupWindow_menu(self):
            setupWindowId = self.command[2]
            textToShow = self.command[3]

            try:
                separator = self.command[5]
            except IndexError:
                separator = "~"

            items = self.command[4].split(separator)

            return self.setupWindowManager.getWindow(setupWindowId).menu(textToShow, items)

        def POL_SetupWindow_Close(self):
            setupWindowId = self.command[2]

            self.setupWindowManager.getWindow(setupWindowId).close()

        def POL_Download(self):
            setupWindowId = self.command[2]
            setupWindow = self.setupWindowManager.getWindow(setupWindowId)

            Downloader(setupWindow).get(self.command[3]).check(self.command[4])
        
        def POL_SetupWindow_licence(self):
            setupWindowId = self.command[2]
            textToShow = self.command[3]
            licenceFile = self.command[5]
            
            self.setupWindowManager.getWindow(setupWindowId).licence(textToShow, File(licenceFile))

        def POL_Throw(self):
            raise ScriptFailureException(self.command[3])
