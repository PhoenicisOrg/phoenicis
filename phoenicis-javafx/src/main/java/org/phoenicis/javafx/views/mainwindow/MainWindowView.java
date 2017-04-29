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

import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;

public class MainWindowView<SideBar extends LeftSideBar> extends Tab {
    protected final ThemeManager themeManager;

    private final BorderPane content;

    private ScrollPane leftContent;

    private HBox waitPanel;
    private FailurePanel failurePanel;

    private Stack<NavigationStep> navigationChronicle;

    private Consumer<Optional<NavigationStep>> onViewChanged;

    public MainWindowView(String text, ThemeManager themeManager) {
        super(text);

        this.themeManager = themeManager;
        this.navigationChronicle = new Stack<NavigationStep>();

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

    public void navigateTo(Node destination) {
        navigateTo(null, destination);
    }

    public void navigateTo(String name, Node destination) {
        if (this.onViewChanged != null) {
            if (this.navigationChronicle.isEmpty()) {
                this.onViewChanged.accept(Optional.empty());
            } else {
                this.onViewChanged.accept(Optional.of(this.navigationChronicle.peek()));
            }
        }

        this.navigationChronicle.push(new NavigationStep(name, destination));

        this.content.setCenter(destination);
    }

    public void navigateToLast() {
        if (this.navigationChronicle.size() >= 2) {
            // delete last step
            this.navigationChronicle.pop();

            NavigationStep last = this.navigationChronicle.pop();

            navigateTo(last.getName().orElse(null), last.getDestination());
        }
    }

    public void clearChronicleNavigateTo(Node destination) {
        clearChronicleNavigateTo(null, destination);
    }

    public void clearChronicleNavigateTo(String name, Node destination) {
        this.navigationChronicle.clear();

        navigateTo(name, destination);
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

    public void setOnViewChanged(Consumer<Optional<NavigationStep>> onViewChanged) {
        this.onViewChanged = onViewChanged;
    }

    public static class NavigationStep {
        private final Optional<String> name;
        private final Node destination;

        public NavigationStep(String name, Node destination) {
            this.name = Optional.ofNullable(name);
            this.destination = destination;
        }

        public Optional<String> getName() {
            return name;
        }

        public Node getDestination() {
            return destination;
        }
    }
}
