package com.playonlinux.ui.impl.javafx;

import com.playonlinux.api.Controller;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.api.UIMessageSender;
import com.playonlinux.api.SetupWindow;
import com.playonlinux.ui.impl.javafx.setupwindow.JavaFXSetupWindowImplementation;
import com.playonlinux.utils.Injectable;


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
