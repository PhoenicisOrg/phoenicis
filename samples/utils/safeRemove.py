from com.playonlinux.framework.templates import Installer
from com.playonlinux.framework import Files
from com.playonlinux.framework import Environment

class Example(Installer):
    title = "Example safe deletePrefix feature"

    def main(self):
        print "Removing a file in playonlinux root"
        Files().mkdir(Environment.getUserRoot() + "/test").remove(Environment.getUserRoot() + "/test")

        print "Removing a file outside playonlinux root"
        Files().mkdir("/tmp/test").remove("/tmp/test")


