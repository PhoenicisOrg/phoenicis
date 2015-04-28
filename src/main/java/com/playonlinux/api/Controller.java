package com.playonlinux.api;

import com.playonlinux.ui.api.EventHandler;

public interface Controller {
    void startApplication();

    SetupWindow createSetupWindowGUIInstance(String title);

    UIMessageSender createUIMessageSender();

    void injectEventHandler(EventHandler eventHandler);
}
