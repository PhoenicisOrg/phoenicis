package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.domain.Script;
import com.playonlinux.ui.api.InstalledApplications;

import java.io.File;

public class PlayOnLinuxEventsImplementation implements EventHandler {
    private InstalledApplications installedApplications;

    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playonlinuxScript.run();
    }

    @Override
    public InstalledApplications getInstalledApplications() throws PlayOnLinuxError {
        if(installedApplications == null) {
            installedApplications = new PlayOnLinuxInstalledApplicationImplementation();
        }
        return this.installedApplications;
    }
}
