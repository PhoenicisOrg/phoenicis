package com.playonlinux.ui.impl.javafx.mainwindow;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.EventHandler;
import com.playonlinux.ui.api.InstalledApplications;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

@Component
public class MainWindow extends Stage {

    @Inject
    static EventHandler eventHandler;
    private ApplicationList listApps;

    public void setUpWindow() {
        MenuBar menuBar =  new MenuBar(this);

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane, 600, 400);

        VBox topContainer = new VBox();
        topContainer.getChildren().add(menuBar);
        topContainer.getChildren().add(new ToolBar());
        pane.setTop(topContainer);

        pane.setBottom(new StatusBar(this, scene));



        GridPane mainContent = new GridPane();


        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setPercentWidth(30);

        ColumnConstraints columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPercentWidth(70);

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setPercentHeight(100);

        mainContent.getColumnConstraints().add(columnConstraint);
        mainContent.getColumnConstraints().add(columnConstraint2);

        mainContent.getRowConstraints().add(rowConstraint);

        mainContent.setVgap(2.0);
        mainContent.add(new MenuLeft(), 0, 0);


        this.listApps = new ApplicationList();


        mainContent.add(listApps, 1, 0);
        pane.setCenter(mainContent);


        this.setScene(scene);
        this.setTitle("PlayOnLinux");
        this.show();
    }

    public void setUpEvents() throws PlayOnLinuxError {
        InstalledApplications installedApplications = eventHandler.getInstalledApplications();
        installedApplications.addObserver(listApps);
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }


}

