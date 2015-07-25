import com.playonlinux.framework.SetupWindowCommander as POL_SetupWindow

setupWindow = POL_SetupWindow("TITLE")

print "Hello from python!"

# -----BEGIN PGP SIGNATURE-----
# Version: GnuPG/MacGPG2 v2.0.17 (Darwin)

# MOCKED SIGNATURE (PYTHON)
# -----END PGP SIGNATURE-----

setupWindow.message("Test\nTest");
setupWindow.message("Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                          "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 " +
                          "Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 Test 2 ");

result = setupWindow.textbox("Test 3");
print result

setupWindow.message("Test 4");
setupWindow.message("Test 5");

