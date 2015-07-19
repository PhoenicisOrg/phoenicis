from com.playonlinux.framework.templates import Installer

class Example(Installer):
    title = "Example textbox"

    def main(self):
        setupWindow = self.getSetupWizard()

        result = setupWindow.textbox("Please enter a text here:")
        setupWindow.message("You have written: %s" % result)

        setupWindow.close()
