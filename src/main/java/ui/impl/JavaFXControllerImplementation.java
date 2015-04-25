package ui.impl;

import api.Controller;
import org.springframework.context.ApplicationContext;
import ui.impl.api.EventHandler;
import api.UIMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import api.SetupWindow;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;

import javax.naming.ldap.Control;

@Component
public class JavaFXControllerImplementation implements Controller {

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
