package app;


import scripts.Script;

import java.io.File;

public class PlayOnLinuxEvents implements ui.api.EventHandler {
    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playonlinuxScript.run();
    }
}
