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

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.LeftSideBar;

public class MainWindowView<SideBar extends LeftSideBar> extends Tab {
    protected final ThemeManager themeManager;

    private final BorderPane content;

    private ScrollPane leftContent;

    private SplitPane mainContent;

    private HBox waitPanel;
    private FailurePanel failurePanel;

    private ObservableList<Node> navigationChronicle;
    private MappedList<Node, Node> closeableChronicle;

    public MainWindowView(String text, ThemeManager themeManager) {
        super(text);

        this.themeManager = themeManager;
        this.navigationChronicle = FXCollections.observableArrayList();
        this.closeableChronicle = new MappedList<>(this.navigationChronicle, item -> {
            if (this.navigationChronicle.indexOf(item) > 0) {
                StackPane result = new StackPane();

                Button closeButton = new Button();
                closeButton.getStyleClass().add("closeIcon");
                closeButton.setOnAction(event -> {
                    this.returnTo(navigationChronicle.indexOf(item) - 1);
                });

                result.setAlignment(Pos.TOP_RIGHT);
                result.getChildren().setAll(item, closeButton);

                StackPane.setMargin(closeButton, new Insets(5, 5, 0, 0));

                return result;
            } else {
                return item;
            }
        });

        this.populateSidebarContainer();
        this.populateMainContainer();
        this.populateFailurePanel();
        this.populateWaitPanel();

        this.content = new BorderPane();
        this.content.getStyleClass().add("mainWindowScene");
        this.content.setLeft(leftContent);
        this.content.setCenter(mainContent);

        Bindings.bindContent(mainContent.getItems(), closeableChronicle);

        this.setContent(content);
    }

    private void populateSidebarContainer() {
        this.leftContent = new ScrollPane();
        this.leftContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.leftContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.leftContent.setBorder(Border.EMPTY);
        this.leftContent.getStyleClass().add("leftPaneScrollbar");
    }

    private void populateMainContainer() {
        this.mainContent = new SplitPane();
        this.mainContent.setBorder(Border.EMPTY);
        this.mainContent.setPadding(new Insets(0));
    }

    private void populateWaitPanel() {
        this.waitPanel = new WaitPanel();
    }

    private void populateFailurePanel() {
        this.failurePanel = new FailurePanel(themeManager);
    }

    protected void setSideBar(SideBar sideBar) {
        this.leftContent.setContent(sideBar);
    }

    public void navigateTo(Node destination) {
        this.navigationChronicle.add(destination);
    }

    public void navigateToAtPosition(int index, Node destination) {
        returnTo(index - 1);
        navigateTo(destination);
    }

    public void clearChronicleNavigateTo(Node destination) {
        this.navigationChronicle.setAll(destination);
    }

    public void returnTo(int index) {
        this.navigationChronicle.subList(index + 1, navigationChronicle.size()).clear();
    }

    public void showWait() {
        clearChronicleNavigateTo(waitPanel);
    }

    public void showFailure() {
        clearChronicleNavigateTo(failurePanel);
    }

    public FailurePanel getFailurePanel() {
        return failurePanel;
    }

    public ThemeManager getThemeManager() { return themeManager; }
}
