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

package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.views.common.ThemeManager;

import java.util.Optional;

/**
 * The MainWindowView is the core component of the Phoenicis JavaFX GUI.
 *
 * It encloses common functionality (e.g. the library) inside a Tab which is presented in the main menu.
 * Every MainWindowView is split into the basic components Sidebar, content and DetailsView.
 *
 * The Sidebar is shown on the left side. It shall allow a quick navigation in the categories which structure the content.
 *
 * The content is the main content which presents the functionality of this particular MainWindowView.
 *
 * The DetailsView is an optional panel on the right side which is opened to show details about currently selected items
 * in the content.
 *
 * @param <SpecificSidebar> Sidebar used by this MainWindowView
 */
public class MainWindowView<SpecificSidebar extends Sidebar> extends Tab {
    protected final ThemeManager themeManager;

    private final BorderPane content;

    protected SpecificSidebar sidebar;

    private HBox waitPanel;
    private FailurePanel failurePanel;

    /**
     * constructor
     * @param text title which is shown in the tab
     * @param themeManager
     */
    public MainWindowView(String text, ThemeManager themeManager) {
        super(text);

        this.themeManager = themeManager;

        this.content = new BorderPane();
        this.content.getStyleClass().add("mainWindowScene");

        this.populateFailurePanel();
        this.populateWaitPanel();

        this.content.setCenter(waitPanel);

        this.setContent(content);
    }

    private void populateWaitPanel() {
        this.waitPanel = new WaitPanel();
    }

    private void populateFailurePanel() {
        this.failurePanel = new FailurePanel();
    }

    /**
     * sets the Sidebar
     * @param sidebar
     */
    protected void setSidebar(SpecificSidebar sidebar) {
        this.sidebar = sidebar;
        this.content.setLeft(this.sidebar);
    }

    /**
     * shows the given Node as content
     * @param nodeToShow
     */
    public void setCenter(Node nodeToShow) {
        closeDetailsView();
        this.content.setCenter(nodeToShow);
    }

    /**
     * shows the given Node in the details view
     * @param nodeToShow
     */
    public void showDetailsView(Node nodeToShow) {
        this.content.setRight(nodeToShow);
    }

    /**
     * closes the details view
     */
    public void closeDetailsView() {
        this.content.setRight(null);
    }

    /**
     * shows the wait panel
     */
    public void showWait() {
        closeDetailsView();
        setCenter(this.waitPanel);
    }

    /**
     * shows the failure panel
     * @param notification notification text
     * @param reason exception which caused the failure
     */
    public void showFailure(String notification, Optional<Exception> reason) {
        closeDetailsView();
        this.failurePanel.setFailure(notification, reason);
        setCenter(this.failurePanel);
    }

    /**
     * returns the failure panel which is shown in case of a failure
     * @return failure panel
     */
    public FailurePanel getFailurePanel() {
        return failurePanel;
    }
}
