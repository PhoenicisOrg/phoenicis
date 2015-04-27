package api;

import ui.api.EventHandler;

public interface Controller {
    void startApplication();

    SetupWindow createSetupWindowGUIInstance(String title);

    UIMessageSender createUIMessageSender();

    void injectEventHandler(EventHandler eventHandler);
}
