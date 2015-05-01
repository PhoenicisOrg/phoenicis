package com.playonlinux.ui.impl.javafx.mainwindow;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Integer.max;

class StatusBar extends javafx.scene.control.ToolBar {
    public StatusBar(Stage stage, Scene scene) {
        ProgressBar progressBar = new ProgressBar();
        Text text = new Text("A new version of PlayOnLinux is available (5.1)");

        text.setWrappingWidth(max(50, (int)scene.getWidth() - 150));
        progressBar.setPrefWidth(130);
        this.getItems().addAll(text, progressBar);

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            text.setWrappingWidth(max(50, newValue.intValue() - 150));
        });
    }
}
