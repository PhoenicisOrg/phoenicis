import scripts.SetupWindowCommander as POL_SetupWindow

setupWindow = POL_SetupWindow("TITLE")

print "Hello from python!"

setupWindow.message("Test\nTest");
setupWindow.message("Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                          "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                          "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 ");

result = setupWindow.textbox("Test 3");
print result

setupWindow.message("Test 4");
setupWindow.message("Test 5");

