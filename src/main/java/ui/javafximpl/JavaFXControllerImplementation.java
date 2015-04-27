package ui.javafximpl;

import api.Controller;
import ui.api.EventHandler;
import api.UIMessageSender;
import api.SetupWindow;
import ui.javafximpl.setupwindow.JavaFXSetupWindowImplementation;
import utils.Injectable;


public class JavaFXControllerImplementation implements Controller {

    @Injectable
    private static EventHandler eventHandler;

    public static EventHandler getStaticEventHandler() {
        return eventHandler;
    }

    public void startApplication() {
        JavaFXApplication.launch(JavaFXApplication.class);
    }

    public SetupWindow createSetupWindowGUIInstance(String title) {
        return new JavaFXSetupWindowImplementation(title);
    }

    @Override
    public UIMessageSender createUIMessageSender() {
        return new JavaFXMessageSenderImplementation();
    }

    @Override
    public void injectEventHandler(EventHandler eventHandler) {
        JavaFXControllerImplementation.eventHandler = eventHandler;
    }
}
