/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

include(["Utils", "Functions", "Filesystem", "Files"]);
include(["Utils", "Functions", "Filesystem", "Extract"]);
include(["Utils", "Functions", "Net", "Download"]);

LATEST_STABLE_VERSION = "1.8.6";

/**
 * Wine main prototype
 * @constructor
 */
function Wine() {
    this._wineWebServiceUrl = Bean("propertyReader").getProperty("webservice.wine.url");
    this._wineEnginesDirectory = Bean("propertyReader").getProperty("application.user.engines") + "/wine";
    this._winePrefixesDirectory = Bean("propertyReader").getProperty("application.user.containers") + "/wineprefix";
    this._configFactory = Bean("compatibleConfigFileFormatFactory");
    this._OperatingSystemFetcher = Bean("operatingSystemFetcher");
    this._wineDebug = "-all";
    this._ldPath = Bean("propertyReader").getProperty("application.environment.ld");
}

/**
 *
 * @param {SetupWizard} [wizard]
 * @returns {SetupWizard|Wine}
 */
Wine.prototype.wizard = function (wizard) {
    // get
    if (arguments.length == 0) {
        return this._wizard;
    }

    // set
    this._wizard = wizard;
    return this;
};

/**
 *
 * @param {string} [debug]
 * @returns {string|Wine}
 */
Wine.prototype.debug = function (debug) {
    // get
    if (arguments.length == 0) {
        return this._wineDebug;
    }

    // set
    this._wineDebug = debug;
    return this;
};

/**
 *
 * @param {string} [architecture]
 * @returns {string|Wine}
 */
Wine.prototype.architecture = function (architecture) {
    // get
    if (arguments.length == 0) {
        return this._architecture;
    }

    // set
    if (this._prefixConfiguration) {
        this._prefixConfiguration.writeValue("wineArchitecture", architecture);
    }

    this._architecture = architecture;
    return this;
};

/**
 *
 * @param {string} [distribution]
 * @returns {string|Wine}
 */
Wine.prototype.distribution = function (distribution) {
    // get
    if (arguments.length == 0) {
        return this._distribution;
    }

    // set
    if (this._prefixConfiguration) {
        this._prefixConfiguration.writeValue("wineDistribution", distribution);
    }

    this._distribution = distribution;
    return this;
};

/**
 *
 * @param {string} [prefix]
 * @returns {string|Wine}
 */
Wine.prototype.prefix = function (prefix) {
    // get
    if (arguments.length == 0) {
        return this._prefix;
    }

    // set
    this._prefix = prefix;
    this.prefixDirectory = this._winePrefixesDirectory + "/" + this._prefix + "/";

    mkdir(this.prefixDirectory);

    this._prefixConfiguration = this._configFactory.open(this.prefixDirectory + "/phoenicis.cfg");

    if (!this._version) {
        this._version = this._prefixConfiguration.readValue("wineVersion");
    } else {
        this._prefixConfiguration.writeValue("wineVersion", this._version);
    }

    if (!this._distribution) {
        this._distribution = this._prefixConfiguration.readValue("wineDistribution", "upstream");
    }

    this._prefixConfiguration.writeValue("wineDistribution", this._distribution);

    if (!this._architecture) {
        this._architecture = this._prefixConfiguration.readValue("wineArchitecture", "x86");
    }

    this._prefixConfiguration.writeValue("wineArchitecture", this._architecture);


    return this;
};

/**
 *
 * @param {string} [directory]
 * @returns {string|Wine}
 */
Wine.prototype.workingDirectory = function (directory) {
    // get
    if (arguments.length == 0) {
        return this._directory;
    }

    // set
    this._directory = directory;
    return this;
};

/**
 *
 * @param executable
 * @param args
 */
Wine.prototype.runInsidePrefix = function (executable, args) {
    return this.run(this.prefixDirectory + "/drive_c/" + executable, args);
};

/**
 *
 * @param executable
 * @param {array} [args = []]
 * @param {boolean} [captureOutput=false]
 * @returns {Wine}
 */
Wine.prototype.run = function (executable, args, captureOutput) {
    if (!args) {
        args = [];
    }

    this._installVersion();

    var wineBinary = this._fetchLocalDirectory() + "/bin/wine";
    var processBuilder = new java.lang.ProcessBuilder(Java.to([wineBinary, executable].concat(args), "java.lang.String[]"));

    if (this._directory) {
        processBuilder.directory(new java.io.File(this._directory));
    } else {
        var driveC = this.prefixDirectory + "/drive_c";
        mkdir(driveC);
        processBuilder.directory(new java.io.File(driveC));
    }

    if (!captureOutput) {
        processBuilder.inheritIO();
    }

    var environment = processBuilder.environment();
    environment.put("WINEPREFIX", this.prefixDirectory);

    if (this._wineDebug) {
        environment.put("WINEDEBUG", this._wineDebug);
    }

    if (this._ldPath) {
        environment.put("LD_LIBRARY_PATH", this._ldPath);
    }

    // this._process = processBuilder.start();

    if (captureOutput) {
        return "";
    } else {
        return this;
    }
};

/**
 * uninstall application
 * @param {string} name of the application which shall be uninstalled
 * @returns {Wine}
 */
Wine.prototype.uninstall = function (application) {
    var list = this.run("uninstaller", ["--list"], true);
    var appEscaped = application.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
    var re = new RegExp("(.*)\\|\\|\\|.*" + appEscaped);
    var uuid = list.match(re);
    if (uuid) {
        this.run("uninstaller", ["--remove", uuid[1]])
            .wait("Please wait while {0} is uninstalled ...".format(application));
    } else {
        print("Could not uninstall {0}!".format(application));
    }
    return this;
};

/**
 * runs "wineboot"
 */
Wine.prototype.create = function () {
    this.run("wineboot");
    return this;
};

/**
 *
 * @returns {string} name of "Program Files"
 */
Wine.prototype.programFiles = function () {
    var programFilesName = this.run("cmd", ["/c", "echo", "%ProgramFiles%"], true).trim();
    if (programFilesName == "%ProgramFiles%") {
        return "Program Files"
    } else {
        return org.apache.commons.io.FilenameUtils.getBaseName(programFilesName);
    }
};

/**
 *
 * @param {string} [wait message = "Please wait..."]
 * @returns {Wine}
 */
Wine.prototype.wait = function (message) {
    if (this._wizard) {
        this._wizard.wait(typeof message !== 'undefined' ? message : "Please wait ...");
    }

    return this._silentWait();
};

/**
 * kill wine server
 * @returns {Wine}
 */
Wine.prototype.kill = function () {
    // this._wineServer("-k");
    return this;
};

/**
 *
 * @returns {Downloader}
 */
Wine.prototype.getAvailableVersions = function () {
    return new Downloader()
        .wizard(this._wizard)
        .url(this._wineWebServiceUrl)
        .get()
};

/**
 *
 * @param {string} [version = LATEST_STABLE_VERSION]
 * @returns {string|Wine}
 */
Wine.prototype.version = function (version) {
    // get
    if (arguments.length == 0) {
        return this._version;
    }

    // set
    if (this._prefixConfiguration) {
        this._prefixConfiguration.writeValue("wineVersion", version);
    }

    this._version = version;
    return this;
};

/**
 *
 * @returns {string} system32 directory
 */
Wine.prototype.system32directory = function () {
    if (fileExists(this.prefixDirectory + "/drive_c/windows/syswow64")) {
        return this.prefixDirectory + "/drive_c/windows/syswow64";
    } else {
        return this.prefixDirectory + "/drive_c/windows/system32";
    }
};

/**
 *
 * @returns {string} system64 directory
 */
Wine.prototype.system64directory = function () {
    if (fileExists(this.prefixDirectory + "/drive_c/windows/syswow64")) {
        return this.prefixDirectory + "/drive_c/windows/system32";
    }
    throw "Prefix seems to be 32bits";
};

/**
 *
 * @returns {string} font directory
 */
Wine.prototype.fontDirectory = function () {
    return this.prefixDirectory + "/drive_c/windows/Fonts";
};

Wine.prototype._installVersion = function () {
    var version = this._version;
    var fullDistributionName = this._fetchFullDistributionName();
    var localDirectory = this._fetchLocalDirectory();
    var wizard = this._wizard;

    if (!fileExists(localDirectory)) {
        print("Installing version: " + this._version);

        var wineJson = JSON.parse(this.getAvailableVersions());

        var that = this;
        wineJson.forEach(function (distribution) {
            if (distribution.name == fullDistributionName) {
                distribution.packages.forEach(function (winePackage) {
                    if (winePackage.version == version) {
                        that._installWinePackage(wizard, winePackage, localDirectory);
                        that._installGecko(wizard, winePackage, localDirectory);
                        that._installMono(wizard, winePackage, localDirectory);
                    }
                });
            }
        });

        // FIXME : Not found case!

    }
};


Wine.prototype._installWinePackage = function (setupWizard, winePackage, localDirectory) {
    var tmpFile = createTempFile("tar.gz");

    new Downloader()
        .wizard(setupWizard)
        .url(winePackage.url)
        .checksum(winePackage.sha1sum)
        .to(tmpFile)
        .get();

    new Extractor()
        .wizard(setupWizard)
        .archive(tmpFile)
        .to(localDirectory)
        .extract();
};

Wine.prototype._installGecko = function (setupWizard, winePackage, localDirectory) {
    var gecko = new Resource()
        .wizard(setupWizard)
        .url(winePackage.geckoUrl)
        .checksum(winePackage.geckoMd5)
        .algorithm("md5")
        .name(winePackage.geckoFile)
        .directory("gecko")
        .get();

    var wineGeckoDir = localDirectory + "/share/wine/gecko";

    lns(new java.io.File(gecko).getParent(), wineGeckoDir);
};

Wine.prototype._installMono = function (setupWizard, winePackage, localDirectory) {
    var mono = new Resource()
        .wizard(setupWizard)
        .url(winePackage.monoUrl)
        .checksum(winePackage.monoMd5)
        .algorithm("md5")
        .name(winePackage.monoFile)
        .directory("mono")
        .get();

    var wineMonoDir = localDirectory + "/share/wine/mono";

    lns(new java.io.File(mono).getParent(), wineMonoDir);
};

Wine.prototype._silentWait = function () {
    return this;
};

Wine.prototype._fetchFullDistributionName = function () {
    var operatingSystem = this._OperatingSystemFetcher.fetchCurrentOperationSystem().getWinePackage();
    return this._distribution + "-" + operatingSystem + "-" + this._architecture;
};

Wine.prototype._fetchLocalDirectory = function () {
    return this._wineEnginesDirectory + "/" + this._fetchFullDistributionName() + "/" + this._version;
};

Wine.prototype._fetchWineServerBinary = function () {
    return this._fetchLocalDirectory() + "/bin/wineserver";
};

Wine.prototype._wineServer = function (parameter) {
    var processBuilder = new java.lang.ProcessBuilder(Java.to([this._fetchWineServerBinary(), parameter], "java.lang.String[]"));
    var environment = processBuilder.environment();
    environment.put("WINEPREFIX", this.prefixDirectory);
    processBuilder.inheritIO();
    // var wineServerProcess = processBuilder.start();
    // wineServerProcess.waitFor();
};

/**
 * runs "regsvr32"
 * @returns {Wine}
 */
Wine.prototype.regsvr32 = function () {
    var _wine = this;

    this.install = function (dll) {
        _wine.run("regsvr32", ["/i", dll])._silentWait();
        return _wine;
    };

    return this;
};

/**
 * Regedit support
 * @param args
 * @returns {Wine}
 */
Wine.prototype.regedit = function () {
    var _wine = this;

    this.open = function (args) {
        _wine.run("regedit", args)._silentWait();
        return _wine;
    };

    this.patch = function (patchContent) {
        if (patchContent.getClass().getCanonicalName() == "byte[]") {
            patchContent = new java.lang.String(patchContent);
        }
        var tmpFile = createTempFile("reg");
        writeToFile(tmpFile, patchContent);
        _wine.run("regedit", [tmpFile])._silentWait();
        return _wine;
    };

    this.fetchValue = function (keyPath) {
        var root = keyPath[0];
        var registryFile;
        switch (root) {
            case "HKEY_CURRENT_USER":
                registryFile = "user.reg";
                break;
            case "HKEY_LOCAL_MACHINE":
                registryFile = "system.reg";
                break;
            default:
                throw "Illegal registry root exception";
        }

        keyPath.shift();

        var registryValue = Bean("registryParser").parseFile(new java.io.File(this.prefixDirectory + "/" + registryFile), root).getChild(keyPath);

        if (registryValue == null) {
            return null;
        }

        if (registryValue.getText) {
            return registryValue.getText();
        } else {
            return registryValue;
        }
    };

    return this;
};

Wine.prototype.registry = Wine.prototype.regedit;

/**
 * sets sound driver
 * @param driver (alsa, pulse)
 * @returns {Wine}
 */
Wine.prototype.setSoundDriver = function (driver) {
    var regeditFileContent =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_CURRENT_USER\\Software\\Wine\\Drivers]\n" +
        "\"Audio\"=\"" + driver + "\"\n";
    this.regedit().patch(regeditFileContent);
    return this;
};

/**
 *
 * @param {boolean} [managed]
 * @returns {boolean|Wine}
 */
Wine.prototype.managed = function (managed) {
    // get
    if (arguments.length == 0) {
        return (this.regedit().fetchValue(["HKEY_CURRENT_USER", "Software", "Wine", "X11 Driver", "Managed"]) == "Y");
    }

    // set
    var managedYn = managed ? "Y" : "N";

    var regeditFileContent =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_CURRENT_USER\\Software\\Wine\\X11 Driver]\n" +
        "\"Managed\"=\"" + managedYn + "\"\n";
    this.regedit().patch(regeditFileContent);
    return this;
};

var SetManagedForApplication = function () {
    var that = this;
    that._regeditFileContent =
        "REGEDIT4\n" +
        "\n";

    that.wine = function(wine) {
        that._wine = wine;
        return that;
    };

    that.set = function(application, managed) {
        var managedYn = managed ? "Y" : "N";

        that._regeditFileContent += "[HKEY_CURRENT_USER\\Software\\Wine\\AppDefaults\\" + application + "\\X11 Driver]\n";
        that._regeditFileContent += "\"Managed\"=\"" + managedYn + "\"\n";

        return that;
    };

    that.do =  function() {
        that._wine.regedit().patch(that._regeditFileContent);
        return that._wine;
    }
};

Wine.prototype.setManagedForApplication = function() {
    return new SetManagedForApplication()
        .wine(this)
};

var OverrideDLL = function () {
    var that = this;
    that._regeditFileContent =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_CURRENT_USER\\Software\\Wine\\DllOverrides]\n";

    that.wine = function (wine) {
        that._wine = wine;
        return that;
    };

    that.set = function (mode, libraries) {
        libraries.forEach(function (library) {
            that._regeditFileContent += "\"*" + library + "\"=\"" + mode + "\"\n";
        });

        return that;
    };

    that.do =  function () {
        that._wine.regedit().patch(that._regeditFileContent);
        return that._wine;
    }
};

Wine.prototype.overrideDLL = function () {
    return new OverrideDLL()
        .wine(this)
};

/**
 * default windows version
 * @param {string} [version (vista, win2003, winxp, win2k, winnt, winme, win98, win95, win31)]
 * @returns {string|Wine}
 */
Wine.prototype.windowsVersion = function (version, servicePack) {
    // get
    if (arguments.length == 0) {
        return this.regedit().fetchValue(["HKEY_CURRENT_USER", "Software", "Wine", "Version"]);
    }

    // set
    var regeditFileContent =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_CURRENT_USER\\Software\\Wine]\n" +
        "\"Version\"=\"" + version + "\"\n";

    if(servicePack) {
        var servicePackNumber = servicePack.replace("sp", "");
        that._regeditFileContent += "[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows NT\\CurrentVersion]";
        that._regeditFileContent += "\"CSDVersion\"=\"Service Pack "+ servicePackNumber +"\"";
        that._regeditFileContent += "[HKEY_LOCAL_MACHINE\\System\\CurrentControlSet\\Control\\Windows]";
        that._regeditFileContent += "\"CSDVersion\"=dword:00000"+servicePackNumber+"00";
    }

    this.regedit().patch(regeditFileContent);
    return this;
};

/**
 * use native application for a certain file extension
 * @param {string} [file extension (pdf, txt, rtf)]
 * @returns {string|Wine}
 */
Wine.prototype.nativeApplication = function (extension) {
    // FIXME: get
    if (arguments.length == 0) {
        return this.regedit().fetchValue(["HKEY_CLASSES_ROOT", "." + extension]);
    }

    // set
    var mimetype = null;
    switch (extension) {
        case "pdf":
            mimetype = "application/pdf";
            break;
        case "txt":
            mimetype = "application/plain";
            break;
        case "rtf":
            mimetype = "application/rtf";
            break;
        default:
            throw "Could not determine mimetype for file extension \"" + extension +"\"";
    }
    var regeditFileContent =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_CLASSES_ROOT\\." + extension + "]\n" +
        "@=\"" + extension + "file\"\n" +
        "\"Content Type\"=\"" + mimetype + "\"\n" +
        "[HKEY_CLASSES_ROOT\\" + extension + "file\\Shell\\Open\\command]\n" +
        "@=\"winebrowser \"%1\"\"";
    this.regedit().patch(regeditFileContent);
    return this;
};

var SetOsForApplication = function () {
    var that = this;
    that._regeditFileContent =
        "REGEDIT4\n" +
        "\n";

    that.wine = function (wine) {
        that._wine = wine;
        return that;
    };

    that.set = function (application, os) {
        that._regeditFileContent += "[HKEY_CURRENT_USER\\Software\\Wine\\AppDefaults\\" + application + "]\n";
        that._regeditFileContent += "\"Version\"=\"" + os + "\"\n";

        return that;
    };

    that.do =  function () {
        that._wine.regedit().patch(that._regeditFileContent);
        return that._wine;
    }
};

Wine.prototype.setOsForApplication = function () {
    return new SetOsForApplication()
        .wine(this)
};

var RegisterFont = function () {
    var that = this;
    that._regeditFileContentNT =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows NT\\CurrentVersion\\Fonts]\n";

    that._regeditFileContent =
        "REGEDIT4\n" +
        "\n" +
        "[HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Fonts]\n";

    that.wine = function (wine) {
        that._wine = wine;
        return that;
    };

    that.set = function (font, file) {
        that._regeditFileContentNT += "\"*" + font + "\"=\"" + file + "\"\n";
        that._regeditFileContent += "\"*" + font + "\"=\"" + file + "\"\n";

        return that;
    };

    that.do =  function () {
        that._wine.regedit().patch(that._regeditFileContentNT);
        that._wine.regedit().patch(that._regeditFileContent);
        return that._wine;
    }
};

Wine.prototype.registerFont = function () {
    return new RegisterFont()
        .wine(this)
};

