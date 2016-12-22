package com.playonlinux.javafx.views.setupwindow;

import com.playonlinux.javafx.UIMessageSenderJavaFXImplementation;
import com.playonlinux.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.SetupWindowUIConfiguration;
import com.playonlinux.scripts.ui.UIMessageSender;
import com.playonlinux.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class JavaFXSetupWindowUIConfiguration implements SetupWindowUIConfiguration {
    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Override
    public SetupWindowFactory setupWindowFactory() {
        return title -> {
            final SetupWindowJavaFXImplementation setupWindow = new SetupWindowJavaFXImplementation(title, toolsConfiguration.operatingSystemFetcher());
            viewsConfigurationLibrary.viewLibrary().createNewTab(setupWindow);
            setupWindow.setOnShouldClose(() -> viewsConfigurationLibrary.viewLibrary().closeTab(setupWindow));
            return setupWindow;
        };
    }

    @Override
    public UIMessageSender uiMessageSender() {
        return new UIMessageSenderJavaFXImplementation();
    }
}
