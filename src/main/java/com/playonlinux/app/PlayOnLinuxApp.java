package com.playonlinux.app;

import com.playonlinux.api.Controller;
import com.playonlinux.scripts.CancelException;
import com.playonlinux.utils.PlayOnLinuxError;

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
