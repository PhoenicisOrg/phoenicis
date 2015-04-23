package app;

import api.Controller;
import scripts.CancelException;
import ui.impl.JavaFXMessageSenderImplementation;
import ui.impl.api.EventHandler;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import scripts.SetupWizard;
import ui.impl.JavaFXControllerImplementation;
import ui.impl.mainwindow.MainWindow;
import utils.OperatingSystem;
import utils.PlayOnLinuxError;
import utils.messages.Message;
import utils.messages.SynchroneousMessage;

@Configuration
@ComponentScan
public class PlayOnLinuxConfig {

    /*
    It is very important to have a lazy initialization here. Otherwise, JavaFX runLater() method will be called before
    JavaFX is initialized, raising a javafx Toolkit not initialized exception
     */
    @Bean
    @Lazy
    MainWindow mainWindow() throws CancelException, InterruptedException {
        return new JavaFXMessageSenderImplementation<MainWindow>().synchroneousSendAndGetResult(
                new SynchroneousMessage<MainWindow>() {
            @Override
            public void execute(Message message) {
                this.setResponse(new MainWindow());
            }
        });
    }

    @Bean
    Controller controller() {
        Controller controller = new JavaFXControllerImplementation();
        SetupWizard.injectMainController(controller);
        return controller;
    }

    @Bean
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
