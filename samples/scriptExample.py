from com.playonlinux.framework.templates import Installer

class Example(Installer):
    title = "Example"

    def main(self):
        setupWindow = self.setupWizard()

        print "Hello from python!"

        setupWindow.message("Test\nTest")
        selectedFile = setupWindow.browse("Select a file", "/", [".exe", ".doc", ".py"])

        setupWindow.message(selectedFile)
        setupWindow.message("Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                                  "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                                  "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 ")

        result = setupWindow.textbox("Test 3")
        print result

        setupWindow.message("Test 4")
        setupWindow.message("Test 5")

        setupWindow.close()

    def rollback(self):
        setupWindow = self.setupWizard()
        setupWindow.message("It seems that everything has crashed. Last chance to rollback")
        Installer.rollback(self)