/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.javafx.views.mainwindow;

import org.phoenicis.javafx.views.mainwindow.ui.LeftSideBar;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MainWindowView extends Tab {
    private final HBox hBox;
    private HBox waitPanel;
    private FailurePanel failurePanel;
    private final LeftSideBar leftContent;

    private Node visiblePane;

    public MainWindowView(String text) {
        super(text);

        hBox = new HBox();
        hBox.getStyleClass().add("mainWindowScene");
        leftContent = new LeftSideBar();
        setContent(hBox);

        waitPanel = new WaitPanel();
        failurePanel = new FailurePanel();
    }

    protected void drawSideBar() {
        hBox.getChildren().add(leftContent);
    }

    protected void clearSideBar() {
        leftContent.getContentChildren().clear();
    }

    protected void addToSideBar(Node... nodes) {
        leftContent.getContentChildren().addAll(nodes);
    }

    public void showRightView(Node nodeToShow) {
        if(visiblePane != null) {
            hBox.getChildren().remove(visiblePane);
        }
        visiblePane = nodeToShow;
        hBox.getChildren().add(visiblePane);
        HBox.setHgrow(visiblePane, Priority.ALWAYS);
    }

    public void showWait() {
        showRightView(waitPanel);
    }

    public void showFailure() {
        showRightView(failurePanel);
    }

    public FailurePanel getFailurePanel() {
        return failurePanel;
    }
}
