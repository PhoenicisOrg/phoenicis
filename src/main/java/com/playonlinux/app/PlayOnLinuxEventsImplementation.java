package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.domain.Script;
import com.playonlinux.ui.api.InstalledApplications;

import java.io.File;

public class PlayOnLinuxEventsImplementation implements EventHandler {
    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playonlinuxScript.run();
    }

    @Override
    public InstalledApplications getInstalledApplications() throws PlayOnLinuxError {
        return new PlayOnLinuxInstalledApplicationImplementation();
    }
}
