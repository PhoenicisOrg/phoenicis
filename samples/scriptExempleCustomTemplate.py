from com.playonlinux.framework.templates import WineSteamInstaller

class Example(WineSteamInstaller):
    logContext = "SteamExample"
    title = "Example with Steam"
    prefix = "Prefix"
    wineversion = "1.7.34"
    steamId = 130
    packages = ["package1", "package2"]
