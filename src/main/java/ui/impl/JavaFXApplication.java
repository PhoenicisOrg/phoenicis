package ui.impl;

import org.springframework.context.ApplicationContext;

import javafx.application.Application;

import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ui.impl.mainwindow.MainWindow;

@Component
public class JavaFXApplication extends Application {

    /*
     To inject the dependency to JavaFX main thread, I haven't found a better way than using a static reference.
     Feel free to improve this
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaFXSpringConfig.class);
        MainWindow mainWindow = applicationContext.getBean(MainWindow.class);

        mainWindow.setUpWindow();
        mainWindow.show();
    }

    public static void launch(ApplicationContext context, Class<JavaFXApplication> javaFXMainWindowClass) {
        launch(javaFXMainWindowClass);
    }
}
