from com.playonlinux.framework.templates import Installer

class Example(Installer):
    title = "Rollback"

    def main(self):
        setupWindow = self.setupWizard()

        setupWindow.message("We are going to make this script crash")
        raise

        setupWindow.close()

    def rollback(self):
        setupWindow = self.setupWizard()
        setupWindow.message("It seems that everything has crashed. Last chance to rollback")
        Installer.rollback(self)