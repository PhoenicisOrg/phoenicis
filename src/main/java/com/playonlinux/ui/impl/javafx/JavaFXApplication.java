package com.playonlinux.ui.impl.javafx;

import com.playonlinux.domain.PlayOnLinuxError;
import javafx.application.Application;

import javafx.stage.Stage;
import com.playonlinux.ui.impl.javafx.mainwindow.MainWindow;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow mainWindow = new MainWindow();

        mainWindow.setUpWindow();
        try {
            mainWindow.setUpEvents();
        } catch (PlayOnLinuxError e) {
            e.printStackTrace();
        }
        mainWindow.show();
    }

}
