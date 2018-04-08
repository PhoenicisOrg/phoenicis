include(["utils", "functions", "quick_script", "online_installer_script"]);

new OnlineInstallerScript()
    .name("Notepad++")
    .editor("Notepad++")
    .applicationHomepage("https://notepad-plus-plus.org/")
    .author("Quentin PÂRIS")
    .url("https://notepad-plus-plus.org/repository/7.x/7.2.2/npp.7.2.2.Installer.exe")
    .checksum("fc20ea01bd98db48b2ff886709e95a4520cfd28c")
    .category("Development")
    .executable("Notepad++.exe")
    .go();
