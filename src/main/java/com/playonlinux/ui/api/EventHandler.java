package com.playonlinux.ui.api;

import com.playonlinux.domain.PlayOnLinuxError;

import java.io.File;

public interface EventHandler {
    void runLocalScript(File scriptToRun);

    InstalledApplications getInstalledApplications() throws PlayOnLinuxError;

    InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxError;
}
