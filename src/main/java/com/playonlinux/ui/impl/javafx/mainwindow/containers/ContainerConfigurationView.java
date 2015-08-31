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

package com.playonlinux.ui.impl.javafx.mainwindow.containers;

import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.ui.api.ProgressControl;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public abstract class ContainerConfigurationView<T> extends VBox implements ProgressControl {
    private final TabPane tabPane;
    private final ToolBar progressPane;
    private final ProgressBar progressBar = new ProgressBar();
    private final Text progressState = new Text();

    public ContainerConfigurationView(T containerEntity) {
        this.tabPane = new TabPane();
        progressPane = new ToolBar(progressState, progressBar);
        this.getChildren().add(tabPane);
        this.getChildren().add(progressPane);

        this.getStyleClass().add("rightPane");
        this.tabPane.getTabs().add(drawInformationTab(containerEntity));
    }

    abstract protected Tab drawInformationTab(T container);

    public List<Tab> getTabs() {
        return tabPane.getTabs();
    }

    public WineWizard getMiniWizard() {
        return new ContainerConfigurationMiniSetupWizard(this);
    }

    @Override
    public void setProgressPercentage(double value) {
        Platform.runLater(() -> progressBar.setProgress(value));
    }

    @Override
    public void setText(String text) {
        Platform.runLater(() -> progressState.setText(text));
    }
}
