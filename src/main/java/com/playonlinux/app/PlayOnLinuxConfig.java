package com.playonlinux.app;

import com.playonlinux.api.Controller;

import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.impl.javafx.ControllerJavaFXImplementation;
import com.playonlinux.domain.PlayOnLinuxError;

import java.io.IOException;

@SuppressWarnings("unused")
public class PlayOnLinuxConfig extends AbstractConfigFile  {

    @Bean
    public Controller controller() {
        return new ControllerJavaFXImplementation();
    }

    @Bean
    public EventHandler eventHandler() {
            return new EventHandlerPlayOnLinuxImplementation();
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
