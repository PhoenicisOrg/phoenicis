from com.playonlinux.framework.templates import Installer

import time

class Example(Installer):
    title = "Example progressbar"

    def main(self):
        setupWindow = self.getSetupWizard()

        progressBar = setupWindow.progressBar("Please wait...")

        for i in xrange(1000):
            progressBar.setProgressPercentage(i / 10.)
            time.sleep(0.01)

        setupWindow.close()
