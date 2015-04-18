#!/usr/bin/env python
from scripts import SetupWizard

class SetupWindowManager(object):
    def __init__(self):
        self.managedWindows = {}

    def getWindow(self, windowId):
        return self.managedWindows[windowId]

    def newWindow(self, windowId, title):
        self.managedWindows[windowId] = SetupWizard(title)
        return self.managedWindows[windowId]


