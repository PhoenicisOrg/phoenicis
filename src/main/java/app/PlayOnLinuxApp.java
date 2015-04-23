package app;

import api.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PlayOnLinuxApp {
    @Autowired
    Controller controller;

    public void start(ApplicationContext context) {
        this.controller.startApplication(context);
    }

    public static void main(String [] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(PlayOnLinuxConfig.class);
        PlayOnLinuxApp application = context.getBean(PlayOnLinuxApp.class);
        application.start(context);
    }

}
