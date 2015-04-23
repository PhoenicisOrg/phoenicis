package ui.impl;

import api.Controller;
import org.springframework.context.ApplicationContext;
import ui.impl.api.EventHandler;
import api.UIMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import api.SetupWindow;
import ui.impl.setupwindow.JavaFXSetupWindowImplementation;

@Component
public class JavaFXControllerImplementation implements Controller {

    public void startApplication(ApplicationContext context) {
        JavaFXApplication.launch(context, JavaFXApplication.class);
    }

    public SetupWindow createSetupWindowGUIInstance(String title) {
        return new JavaFXSetupWindowImplementation(title);
    }

    @Override
    public UIMessageSender createUIMessageSender() {
        return new JavaFXMessageSenderImplementation();
    }
}
