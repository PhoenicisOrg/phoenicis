package com.playonlinux.ui.impl.javafx;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.api.InstalledApplications;

import java.io.File;

@Component
public class JavaFXEventHandler {
    @Inject
    static EventHandler mainEventHandler;


    public InstalledApplications getInstalledApplications() throws PlayOnLinuxError {
        return mainEventHandler.getInstalledApplications();
    }

    public void runLocalScript(File scriptToRun) {
        mainEventHandler.runLocalScript(scriptToRun);
    }

    public void openConfigureWindow() {
        System.out.println("TODO");
    }
}
