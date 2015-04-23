package ui.impl;

import app.PlayOnLinuxConfig;
import org.springframework.context.annotation.*;
import scripts.CancelException;
import ui.impl.mainwindow.MainWindow;
import ui.impl.mainwindow.MenuBar;
import utils.messages.Message;
import utils.messages.SynchroneousMessage;

@Configuration
@ComponentScan
@Import(PlayOnLinuxConfig.class)
public class JavaFXSpringConfig {

    @Bean
    MainWindow mainWindow() throws CancelException, InterruptedException {
        return new JavaFXMessageSenderImplementation<MainWindow>().synchroneousSendAndGetResult(
                new SynchroneousMessage() {
                    @Override
                    public void execute(Message message) {
                        setResponse(new MainWindow());
                    }
                });
    }

    @Bean
    MenuBar mainMenuBar() {
        return new MenuBar();
    }

}
