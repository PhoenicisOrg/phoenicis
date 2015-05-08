package com.playonlinux.ui.impl.javafx;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.api.InstalledApplications;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.ui.impl.configurewindow.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.configurewindow.ConfigureWindow;
import com.playonlinux.ui.impl.javafx.installwindow.InstallWindow;

import java.io.File;

@Component
public class JavaFXEventHandler {
    @Inject
    static EventHandler mainEventHandler;


    public InstalledApplications getInstalledApplications() throws PlayOnLinuxError {
        return mainEventHandler.getInstalledApplications();
    }

    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxError {
        return mainEventHandler.getInstalledVirtualDrives();
    }

    public void runLocalScript(File scriptToRun) {
        mainEventHandler.runLocalScript(scriptToRun);
    }

    public PlayOnLinuxWindow openConfigureWindow(PlayOnLinuxWindow parent, String selectedApplication)
            throws PlayOnLinuxError {
        return ConfigureWindow.getInstance(parent);
    }

    public PlayOnLinuxWindow openInstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        return InstallWindow.getInstance(parent);
    }
}
