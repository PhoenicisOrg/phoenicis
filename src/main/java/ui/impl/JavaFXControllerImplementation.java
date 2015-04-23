package ui.impl;

import api.Controller;
import api.UIMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import api.SetupWindow;
import ui.impl.mainwindow.JavaFXMainWindow;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;

@Component
public class JavaFXControllerImplementation implements Controller {

    @Autowired
    ui.api.EventHandler eventHandler;

    public void startApplication() {
        // For the moment, I have no cleaner way to do that, JavaFX scene is started in a static context
        JavaFXMainWindow.injectEventHandler(this.eventHandler);

        JavaFXMainWindow.launch(JavaFXMainWindow.class);
    }

    public SetupWindow createSetupWindowGUIInstance(String title) {
        return new JavaFXSetupWindowImplementation(title);
    }

    @Override
    public UIMessageSender createUIMessageSender() {
        return new JavaFXMessageSenderImplementation();
    }
}
