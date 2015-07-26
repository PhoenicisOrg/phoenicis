from com.playonlinux.framework.templates import Installer
from com.playonlinux.core.utils import FileAnalyser

from java.io import File

class Example(Installer):
    title = "Example mimetype"

    def main(self):
        setupWindow = self.getSetupWizard()

        selectedFile = setupWindow.browse("Select a file")

        setupWindow.message("Calculated mimetype of %s: \nMimetype: %s\nDescription: %s" %
                            (selectedFile, FileAnalyser.getMimetype(File(selectedFile)), FileAnalyser.getDescription(File(selectedFile))))

        setupWindow.close()
