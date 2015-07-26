from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import WinePrefix

class Example(Installer):
    title = "Example wine installation"

    def main(self):
        setupWizard = self.getSetupWizard()

        WinePrefix(self.getSetupWizard()).select("TestPrefix").runBackground("winecfg").waitExit()

        setupWizard.close()
