package com.playonlinux.ui.impl.javafx;

import javafx.application.Application;

import javafx.stage.Stage;
import com.playonlinux.ui.impl.javafx.mainwindow.JavaFXMainWindowImplementation;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JavaFXMainWindowImplementation mainWindow = new JavaFXMainWindowImplementation();

        mainWindow.setUpWindow();
        mainWindow.show();
    }

}
