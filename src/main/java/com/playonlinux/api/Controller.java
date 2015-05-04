package com.playonlinux.api;

public interface Controller {
    void startApplication();

    SetupWindow createSetupWindowGUIInstance(String title);

    UIMessageSender createUIMessageSender();

}
