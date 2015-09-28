from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import Downloader

class Example(Installer):
    title = "Download example"

    def main(self):
        setupWizard = self.setupWizard()

        Downloader.wizard(setupWizard).get("http://download.spotify.com/Spotify%20Installer.exe", "/tmp/test.exe")

        setupWizard.close()
