package com.playonlinux.ui.impl.javafx.mainwindow;

import com.playonlinux.ui.impl.javafx.JavaFXControllerImplementation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.playonlinux.ui.api.EventHandler;

public class JavaFXMainWindowImplementation extends Stage {


    public void setUpWindow() {
        MenuBar menuBar =  new MenuBar(this);

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane, 600, 400);

        VBox topContainer = new VBox();
        topContainer.getChildren().add(menuBar);
        topContainer.getChildren().add(new ToolBar());
        pane.setTop(topContainer);

        pane.setBottom(new StatusBar(this, scene));

        /*
        GridPane mainContent = new GridPane();


        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setPercentWidth(30);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(100);

        mainContent.getColumnConstraints().add(columnConstraint);
        mainContent.getRowConstraints().add(rowConstraint);

        mainContent.setVgap(2.0);
        mainContent.add(new MenuLeft(), 0, 0);

        pane.setCenter(mainContent);
        */

        this.setScene(scene);
        this.setTitle("PlayOnLinux");
        this.show();
    }

    public EventHandler getEventHandler() {
        return JavaFXControllerImplementation.getStaticEventHandler();
    }

}

