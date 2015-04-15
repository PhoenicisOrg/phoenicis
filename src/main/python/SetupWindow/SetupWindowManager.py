#!/usr/bin/env python
from scripts import SetupWindowCommander

class SetupWindowManager(object):
    def __init__(self):
        self.managedWindows = {}

    def getWindow(self, windowId):
        return self.managedWindows[windowId]

    def newWindow(self, windowId, title):
        self.managedWindows[windowId] = SetupWindowCommander(title)
        return self.managedWindows[windowId]


