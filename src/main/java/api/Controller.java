package api;

import ui.impl.api.EventHandler;

public interface Controller {
    void startApplication();

    SetupWindow createSetupWindowGUIInstance(String title);

    UIMessageSender createUIMessageSender();

    void injectEventHandler(EventHandler eventHandler);
}
