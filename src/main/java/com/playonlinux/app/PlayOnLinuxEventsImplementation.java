package com.playonlinux.app;

import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.framework.Script;

import java.io.File;

public class PlayOnLinuxEventsImplementation implements EventHandler {
    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playonlinuxScript.run();
    }
}
