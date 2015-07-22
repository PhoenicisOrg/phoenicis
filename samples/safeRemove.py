from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import WineInstallation

class Example(Installer):
    title = "Example wine installation"

    def main(self):
        setupWizard = self.getSetupWizard()

        wineInstallation = WineInstallation("1.7.36", "upstream-x86", setupWizard)
        wineInstallation.install()

        setupWizard.close()
