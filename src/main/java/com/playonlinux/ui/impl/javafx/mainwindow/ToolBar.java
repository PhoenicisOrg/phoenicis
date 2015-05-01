package com.playonlinux.ui.impl.javafx.mainwindow;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import static com.playonlinux.utils.Localisation.translate;

class ToolBar extends javafx.scene.control.ToolBar {

    public ToolBar() {
        ImageView runImage = new ImageView(this.getClass().getResource("run.png").toExternalForm());
        runImage.setFitWidth(16);
        runImage.setFitHeight(16);

        ImageView stopImage = new ImageView(this.getClass().getResource("stop.png").toExternalForm());
        stopImage.setFitWidth(16);
        stopImage.setFitHeight(16);

        ImageView installImage = new ImageView(this.getClass().getResource("install.png").toExternalForm());
        installImage.setFitWidth(16);
        installImage.setFitHeight(16);

        ImageView removeImage = new ImageView(this.getClass().getResource("delete.png").toExternalForm());
        removeImage.setFitWidth(16);
        removeImage.setFitHeight(16);

        ImageView configureImage = new ImageView(this.getClass().getResource("configure.png").toExternalForm());
        configureImage.setFitWidth(16);
        configureImage.setFitHeight(16);


        Button run = new Button(translate("Run"), runImage);
        run.setContentDisplay(ContentDisplay.LEFT);

        Button stop = new Button(translate("Stop"), stopImage);
        stop.setContentDisplay(ContentDisplay.LEFT);

        Button install = new Button(translate("Install"), installImage);
        install.setContentDisplay(ContentDisplay.LEFT);

        Button remove = new Button(translate("Remove"), removeImage);
        remove.setContentDisplay(ContentDisplay.LEFT);

        Button configure = new Button(translate("Configure"), configureImage);
        configure.setContentDisplay(ContentDisplay.LEFT);

        TextField searchField = new TextField();


        this.getItems().addAll(
                run,
                stop,
                new Separator(),
                install,
                remove,
                new Separator(),
                configure,
                new Separator(),
                searchField
        );
    }

}
