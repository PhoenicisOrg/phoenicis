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

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.LeftSideBar;

public class MainWindowView<SideBar extends LeftSideBar> extends Tab {
    protected final ThemeManager themeManager;

    private final BorderPane content;

    private ScrollPane leftContent;

    private HBox waitPanel;
    private FailurePanel failurePanel;

    public MainWindowView(String text, ThemeManager themeManager) {
        super(text);

        this.themeManager = themeManager;

        this.populateSidebarContainer();
        this.populateFailurePanel();
        this.populateWaitPanel();

        this.content = new BorderPane();
        this.content.getStyleClass().add("mainWindowScene");
        this.content.setLeft(leftContent);
        this.content.setCenter(waitPanel);

        this.setContent(content);
    }

    private void populateSidebarContainer() {
        this.leftContent = new ScrollPane();
        this.leftContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.leftContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.leftContent.setBorder(Border.EMPTY);
        this.leftContent.getStyleClass().add("leftPaneScrollbar");
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

    public void showRightView(Node nodeToShow) {
        this.content.setCenter(nodeToShow);
    }

    public void showDetailsView(Node nodeToShow) { this.content.setRight(nodeToShow); }

    public void showWait() {
        showRightView(waitPanel);
    }

    public void showFailure() {
        showRightView(failurePanel);
    }

    public FailurePanel getFailurePanel() {
        return failurePanel;
    }

    public ThemeManager getThemeManager() { return themeManager; }
}
