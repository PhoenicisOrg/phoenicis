package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.domain.Script;
import com.playonlinux.ui.api.InstalledApplications;
import com.playonlinux.ui.api.InstalledVirtualDrives;

import java.io.File;

@Component
public class EventHandlerPlayOnLinuxImplementation implements EventHandler {

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private InstalledApplications installedApplications;
    private InstalledVirtualDrivesPlayOnLinuxImplementation virtualDrives;

    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playOnLinuxBackgroundServicesManager.register(playonlinuxScript);
    }

    @Override
    public InstalledApplications getInstalledApplications() throws PlayOnLinuxError {
        if(installedApplications == null) {
            installedApplications = new InstalledApplicationsPlayOnLinuxImplementation();
        }
        return this.installedApplications;
    }

    @Override
    public InstalledVirtualDrives getInstalledVirtualDrives() throws PlayOnLinuxError {
        if(virtualDrives == null) {
            virtualDrives = new InstalledVirtualDrivesPlayOnLinuxImplementation();
        }

        return this.virtualDrives;
    }
}
