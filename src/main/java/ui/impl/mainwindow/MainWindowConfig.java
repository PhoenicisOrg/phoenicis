package ui.impl.mainwindow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import scripts.CancelException;
import ui.impl.JavaFXMessageSenderImplementation;
import utils.messages.Message;
import utils.messages.SynchroneousMessage;

@Configuration
@ComponentScan
@Import({app.PlayOnLinuxConfig.class})
public class MainWindowConfig {

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
