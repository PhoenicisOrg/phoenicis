from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import WineVersion

class Example(Installer):
    title = "Example wine installation"

    def main(self):
        setupWizard = self.getSetupWizard()

        version = setupWizard.textbox("Choose a version", "1.7.36")
        distribution = setupWizard.textbox("Choose a distribution", "upstream-x86")

        wineInstallation = WineVersion(version, distribution, setupWizard)
        wineInstallation.install()

        setupWizard.close()
