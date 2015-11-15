from com.playonlinux.framework import SetupWizard, WineShortcut, Environment

import unittest, os
import com.playonlinux.core.python.JsonCodec as json

class TestCreateShortcut(unittest.TestCase):
    def testCreateShortcut(self):
        setupWizard = SetupWizard("Mock setup wizard")
        setupWizard.init()

        WineShortcut.wizard(setupWizard).withName("Shortcut name")\
            .withExecutableName("winecfg")\
            .withArguments(["argument 1", "argument 2"])\
            .withWineDebug("-all")\
            .withWorkingDirectory("Working directory")\
            .withWinePrefix("Wine prefix")\
            .create()

        shortcuts = Environment.getShortcutsPath()
        shortcut = os.path.join(shortcuts, "Shortcut name")

        shortcutFileContent = open(shortcut, "r").read()

        shortcutJson = json.loads(shortcutFileContent)

        self.assertEquals("com.playonlinux.library.shortcuts.WineShortcut", shortcutJson["@class"])
        self.assertEquals("-all", shortcutJson["wineDebug"])
        self.assertEquals("Wine prefix", shortcutJson["winePrefix"])
        self.assertEquals("Working directory", shortcutJson["workingDirectory"])
        self.assertEquals("winecfg", shortcutJson["executableName"])
        self.assertEquals("argument 1", shortcutJson["arguments"][0])
        self.assertEquals("argument 2", shortcutJson["arguments"][1])

        setupWizard.close()

