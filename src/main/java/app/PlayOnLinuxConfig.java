package app;

import api.Controller;
import org.springframework.context.annotation.*;
import scripts.SetupWizard;
import ui.impl.JavaFXControllerImplementation;

@Configuration
@ComponentScan
public class PlayOnLinuxConfig {

    @Bean
    Controller gui() {
        Controller controller = new JavaFXControllerImplementation();
        SetupWizard.injectMainController(controller);
        return controller;
    }

    @Bean
    ui.api.EventHandler eventHandler() { return new PlayOnLinuxEvents(); }
}
