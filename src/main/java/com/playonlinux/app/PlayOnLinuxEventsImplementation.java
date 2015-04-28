package com.playonlinux.app;

import org.springframework.stereotype.Component;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.scripts.Script;

import java.io.File;

@Component
public class PlayOnLinuxEventsImplementation implements EventHandler {
    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playonlinuxScript.run();
    }
}
