from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import Wine

class Example(Installer):
    title = "Example wine installation"

    def main(self):
        setupWizard = self.getSetupWizard()

        Wine.wizard(self.getSetupWizard()).selectPrefix("TestPrefix").createPrefix("1.7.33", "upstream").overrideDlls({"gdiplus":"builtin"}).runBackground("winecfg").waitExit()

        setupWizard.close()
