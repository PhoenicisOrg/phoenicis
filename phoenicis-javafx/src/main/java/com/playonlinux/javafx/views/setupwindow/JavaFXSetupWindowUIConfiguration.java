package com.playonlinux.javafx.views.setupwindow;

import com.playonlinux.javafx.UIMessageSenderJavaFXImplementation;
import com.playonlinux.javafx.views.common.ConfirmMessage;
import com.playonlinux.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.SetupWindowUIConfiguration;
import com.playonlinux.scripts.ui.UIMessageSender;
import com.playonlinux.scripts.ui.UIQuestionFactory;
import com.playonlinux.tools.ToolsConfiguration;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaFXSetupWindowUIConfiguration implements SetupWindowUIConfiguration {
    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Override
    @Bean
    public SetupWindowFactory setupWindowFactory() {
        return title -> {
            final SetupWindowJavaFXImplementation setupWindow = new SetupWindowJavaFXImplementation(title, toolsConfiguration.operatingSystemFetcher());
            viewsConfigurationLibrary.viewLibrary().createNewTab(setupWindow);
            setupWindow.setOnShouldClose(() -> viewsConfigurationLibrary.viewLibrary().closeTab(setupWindow));
            return setupWindow;
        };
    }

    @Override
    @Bean
    public UIMessageSender uiMessageSender() {
        return new UIMessageSenderJavaFXImplementation();
    }

    @Override
    @Bean
    public UIQuestionFactory uiQuestionFactory() {
        return (text, yesCallback, noCallback) -> Platform.runLater(() -> new ConfirmMessage("Question", text).ask(yesCallback, noCallback));
    }
}
