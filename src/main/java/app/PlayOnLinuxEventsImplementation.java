package app;

import org.springframework.stereotype.Component;
import ui.impl.api.EventHandler;
import scripts.Script;

import java.io.File;

@Component
public class PlayOnLinuxEventsImplementation implements EventHandler {
    public void runLocalScript(File scriptToRun) {
        Script playonlinuxScript = new Script(scriptToRun);
        playonlinuxScript.run();
    }
}
