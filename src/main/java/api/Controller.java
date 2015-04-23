package api;

import org.springframework.context.ApplicationContext;

public interface Controller {
    void startApplication(ApplicationContext context);

    SetupWindow createSetupWindowGUIInstance(String title);

    UIMessageSender createUIMessageSender();
}
