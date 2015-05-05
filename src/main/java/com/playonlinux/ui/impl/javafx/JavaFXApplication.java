package com.playonlinux.ui.impl.javafx;

import com.playonlinux.domain.PlayOnLinuxError;
import javafx.application.Application;

import javafx.stage.Stage;
import com.playonlinux.ui.impl.javafx.mainwindow.MainWindow;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow();

        mainWindow.setUpWindow();
        try {
            mainWindow.setUpEvents();
        } catch (PlayOnLinuxError e) {
            // FIXME
            e.printStackTrace();
        }
        mainWindow.show();
    }

}
