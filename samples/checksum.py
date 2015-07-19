from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import Checksum

class Example(Installer):
    title = "Example checksum"

    def main(self):
        setupWindow = self.getSetupWizard()

        selectedFile = setupWindow.browse("Select a file")
        progressBar = setupWindow.progressBar("Please wait ...")

        checkSum = Checksum().progress(progressBar).md5(selectedFile)

        setupWindow.message("Checksum of %s: %s" % (selectedFile, checkSum))

        setupWindow.close()
