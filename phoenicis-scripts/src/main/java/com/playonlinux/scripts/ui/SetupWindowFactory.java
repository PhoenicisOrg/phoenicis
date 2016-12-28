package com.playonlinux.scripts.ui;

@FunctionalInterface
public interface SetupWindowFactory {
    SetupWindow createSetupWindow(String title);
}
