import scripts.SetupWindowCommander as POL_SetupWindow

setupWindow = POL_SetupWindow("TITLE")

print setupWindow.message("Test\n Test");
print setupWindow.textbox("Test 2");
setupWindow.message("Test 3");
setupWindow.message("Test 4");
setupWindow.message("Test 5");

print "Hello from python!"
