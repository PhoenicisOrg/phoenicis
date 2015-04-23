package app;

import api.Controller;

import ui.impl.api.EventHandler;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import scripts.SetupWizard;
import ui.impl.JavaFXControllerImplementation;
import utils.OperatingSystem;
import utils.PlayOnLinuxError;


@Configuration
@ComponentScan
public class PlayOnLinuxConfig {

    @Bean
    Controller controller() {
        Controller controller = new JavaFXControllerImplementation();
        SetupWizard.injectMainController(controller);
        return controller;
    }

    @Bean
    @Lazy
    EventHandler eventHandler() {
        return new PlayOnLinuxEventsImplementation();
    }

    @Bean
    public static PropertyPlaceholderConfigurer properties() throws PlayOnLinuxError {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

        Resource[] resources;

        switch (OperatingSystem.fetchCurrentOperationSystem() ) {
            case MACOSX:
                resources = new ClassPathResource[] {
                        new ClassPathResource("playonmac.properties")
                };
                break;
            case LINUX:
            default:
                resources = new ClassPathResource[] {
                        new ClassPathResource("playonlinux.properties")
                };
                break;

        }
        propertyPlaceholderConfigurer.setLocations(resources);
        propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertyPlaceholderConfigurer;
    }
}
