from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import Wine

class Example(Installer):
    title = "Example wine installation"

    def main(self):
        setupWizard = self.getSetupWizard()

        Wine.wizard(self.getSetupWizard()).selectPrefix("TestPrefix").runBackground("winecfg").waitExit()

        setupWizard.close()
