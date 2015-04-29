package com.playonlinux.app;

import com.playonlinux.api.Controller;

import com.playonlinux.scripts.SetupWizard;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.javafximpl.JavaFXControllerImplementation;
import com.playonlinux.utils.PlayOnLinuxError;
import com.playonlinux.wine.WineProcessBuilder;

import java.io.IOException;

public class PlayOnLinuxConfig {

    static Controller controller;
    /*
    This is the only way I've found to inject dependencies through JavaFX and Jython.
    Feel free to improve this piece of code
     */

    /*
    Configuration
     */
    static Controller controller() {
        Controller controller = new JavaFXControllerImplementation();
        return controller;
    }

    static EventHandler eventHandler() {
            return new PlayOnLinuxEventsImplementation();
    }

    static PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxError, IOException {
        return new PlayOnLinuxContext();
    }

    /*
    Get static instances
     */
    public Controller getControllerInstance() {
        if(controller == null) {
            controller = controller();
        }
        return controller;
    }

    /*
    Injection
     */
    public void Inject() throws PlayOnLinuxError, IOException {
        Controller controller = controller();
        PlayOnLinuxContext playOnLinuxContext = playOnLinuxContext();

        controller.injectEventHandler(eventHandler());
        SetupWizard.injectMainController(controller);

        com.playonlinux.scripts.WinePrefix.injectPlayOnLinuxContext(playOnLinuxContext);

        WineProcessBuilder.injectApplicationEnvironment(playOnLinuxContext.getSystemEnvironment());
    }



}
