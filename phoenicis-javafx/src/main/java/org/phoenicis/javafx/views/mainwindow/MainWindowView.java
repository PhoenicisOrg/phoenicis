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
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    public MainWindowView(String text, ThemeManager themeManager) {
        super(text);

        this.themeManager = themeManager;
        this.navigationChronicle = FXCollections.observableArrayList();

        this.populateSidebarContainer();
        this.populateMainContainer();
        this.populateFailurePanel();
        this.populateWaitPanel();

        this.content = new BorderPane();
        this.content.getStyleClass().add("mainWindowScene");
        this.content.setLeft(leftContent);
        this.content.setCenter(mainContent);

        Bindings.bindContent(mainContent.getItems(), navigationChronicle);

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
