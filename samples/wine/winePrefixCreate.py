from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import Wine

class Example(Installer):
    title = "Example wine installation"

    def main(self):
        setupWizard = self.setupWizard()

        Wine.wizard(self.setupWizard()).selectPrefix("TestPrefix").createPrefix("1.7.36")

        Wine.wizard(self.setupWizard()).selectPrefix("TestPrefix").createPrefix("1.7.36")
        Wine.wizard(self.setupWizard()).selectPrefix("TestPrefix2").createPrefix("1.4.1", "upstream")

        setupWizard.close()
