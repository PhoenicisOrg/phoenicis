from com.playonlinux.framework import SetupWizard
from com.playonlinux.framework.templates import Installer

class Example(Installer):
    logContext = "ExampleScript"

    def main(self):
        setupWindow = SetupWizard("TITLE")

        print "Hello from python!"

        setupWindow.message("Test\nTest")
        setupWindow.message("Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                                  "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                                  "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 ")

        result = setupWindow.textbox("Test 3")
        print result

        setupWindow.message("Test 4")
        setupWindow.message("Test 5")

        setupWindow.close()

