/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.ui.impl.javafx.mainwindow;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.InstalledApplications;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.MalformedURLException;

import static com.playonlinux.domain.Localisation.translate;

public class MainWindow extends Stage implements PlayOnLinuxWindow {
    private MainWindowEventHandler mainEventHandler = new MainWindowEventHandler();
    private ApplicationListWidget applicationListWidget;
    private ToolBar toolBar;
    private StatusBar statusBar;

    public void setUpWindow() {
        MenuBar menuBar =  new MenuBar(this);

        BorderPane pane = new BorderPane();
        Scene scene = new Scene(pane, 600, 400);

        VBox topContainer = new VBox();
        toolBar = new ToolBar(this);

        topContainer.getChildren().add(menuBar);
        topContainer.getChildren().add(toolBar);
        pane.setTop(topContainer);

        statusBar = new StatusBar(this, scene);
        pane.setBottom(statusBar);



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


        this.applicationListWidget = new ApplicationListWidget();


        mainContent.add(applicationListWidget, 1, 0);
        pane.setCenter(mainContent);


        this.setScene(scene);
        this.setTitle("PlayOnLinux");
        this.show();
    }

    public void setUpEvents() throws PlayOnLinuxError {
        InstalledApplications installedApplications = mainEventHandler.getInstalledApplications();
        installedApplications.addObserver(applicationListWidget);

        toolBar.setUpEvents();

        try {
            statusBar.setUpEvents();
        } catch (MalformedURLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translate("Error while trying to update installer list."));
            alert.setContentText("The error was: MalformedURLException");
            e.printStackTrace();
        }
    }



    protected MainWindowEventHandler getMainEventHandler() {
        return mainEventHandler;
    }


    public String getSelectedApplication() {
        return null;
    }
}

