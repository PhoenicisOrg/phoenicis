from com.playonlinux.framework.templates import Installer
from com.playonlinux.core.utils import ExeAnalyser

from java.io import File

class Example(Installer):
    title = "Example Exe analyser"

    def main(self):
        setupWindow = self.getSetupWizard()

        selectedFile = setupWindow.browse("Select a file")

        setupWindow.message("Result for %s: \nIs 64bits: %s" %
                            (selectedFile, ExeAnalyser.is64Bits(File(selectedFile))))

        setupWindow.close()
