package app;

import api.Controller;

import scripts.SetupWizard;
import ui.impl.api.EventHandler;
import ui.impl.JavaFXControllerImplementation;

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
    public void Inject() {
        Controller controller = controller();
        controller.injectEventHandler(eventHandler());
        SetupWizard.injectMainController(controller);
    }

}
