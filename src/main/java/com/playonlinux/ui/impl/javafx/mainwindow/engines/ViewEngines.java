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

package com.playonlinux.ui.impl.javafx.mainwindow.engines;

import com.playonlinux.dto.ui.engines.EnginesWindowDTO;
import com.playonlinux.ui.impl.javafx.mainwindow.*;
import com.playonlinux.utils.observer.Observable;
import com.playonlinux.utils.observer.Observer;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.log4j.Logger;

import static com.playonlinux.lang.Localisation.translate;

public class ViewEngines extends MainWindowView implements Observer<Observable, EnginesWindowDTO> {
    private static final Logger LOGGER = Logger.getLogger(ViewEngines.class);
    private final EventHandlerEngines eventHandlerLibrary;
    private TextField searchBar;



    public ViewEngines(MainWindow parent) {
        super(parent);

        eventHandlerLibrary = new EventHandlerEngines();

        this.drawSideBar();
        this.drawContent();
    }

    private void drawContent() {
        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("new tab");
        tab.setContent(new Rectangle(200, 200, Color.LIGHTSTEELBLUE));
        tabPane.getTabs().add(tab);

    }

    protected void drawSideBar() {
        super.drawSideBar();

        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> applyFilter(searchBar.getText()));

        LeftButton wine = new LeftButton("/com/playonlinux/ui/impl/javafx/mainwindow/engines/wine.png", "Wine");

        wine.getStyleClass().add("leftPaneButtons");

        LeftSpacer spacer = new LeftSpacer();
        addToSideBar(searchBar, spacer, new LeftBarTitle("Engines"), wine);
    }

    private void applyFilter(String searchText) {

    }

    public void setUpEvents() {

    }

    public EventHandlerEngines getEventHandler() {
        return eventHandlerLibrary;
    }

    @Override
    public void update(Observable observable, EnginesWindowDTO argument) {

    }
}
