package app;

import api.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import scripts.CancelException;
import utils.PlayOnLinuxError;

import java.io.IOException;

public class PlayOnLinuxApp {

    public void start() throws PlayOnLinuxError, IOException {
        PlayOnLinuxConfig playOnLinuxConfig = new PlayOnLinuxConfig();
        playOnLinuxConfig.Inject();

        Controller controller = playOnLinuxConfig.getControllerInstance();
        controller.startApplication();
    }

    public static void main(String [] args) throws CancelException, InterruptedException, PlayOnLinuxError, IOException {
        PlayOnLinuxApp application =  new PlayOnLinuxApp();
        application.start();
    }

}
