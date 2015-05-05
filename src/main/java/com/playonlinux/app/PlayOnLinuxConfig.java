package com.playonlinux.app;

import com.playonlinux.api.Controller;

import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.impl.javafx.JavaFXControllerImplementation;
import com.playonlinux.domain.PlayOnLinuxError;

import java.io.IOException;

public class PlayOnLinuxConfig extends AbstractConfigFile  {

    @Bean
    public Controller controller() {
        return new JavaFXControllerImplementation();
    }

    @Bean
    public EventHandler eventHandler() {
            return new PlayOnLinuxEventsImplementation();
    }

    @Bean
    public PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxError, IOException {
        return new PlayOnLinuxContext();
    }

    @Bean
    public PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager() {
        return new PlayOnLinuxBackgroundServicesManager();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }
}
