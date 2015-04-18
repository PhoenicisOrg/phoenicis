package ui.impl;

import api.Controller;
import api.UIMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import api.SetupWindow;
import ui.impl.setupwindow.SetupWindowImplementation;

@Component
public class JavaFXControllerImplementation implements Controller {

    @Autowired
    ui.api.EventHandler eventHandler;

    public void startApplication() {
        // For the moment, I have no cleaner way to do that, JavaFX scene is started in a static context
        MainWindow.defineStaticEventHandler(this.eventHandler);

        MainWindow.launch(MainWindow.class);
    }

    public SetupWindow createSetupWindowGUIInstance(String title) {
        return new SetupWindowImplementation(title);
    }

    @Override
    public UIMessageSender createUIMessageSender() {
        return new JavaFXMessageSenderImplementation();
    }
}
