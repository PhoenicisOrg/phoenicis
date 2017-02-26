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

package org.phoenicis.javafx.views.scriptui;

import org.phoenicis.scripts.ui.MenuItem;
import org.phoenicis.scripts.ui.Message;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.List;


public class StepRepresentationMenu extends StepRepresentationMessage {
    private final String defaultValue;
    private final List<String> menuItems;
    private final ListView<String> listViewWidget;
    private final Message<MenuItem> messageWaitingForResponse;

    public StepRepresentationMenu(SetupUiJavaFXImplementation parent, Message<MenuItem> messageWaitingForResponse, String textToShow, List<String> menuItems, String defaultValue) {
        super(parent, messageWaitingForResponse, textToShow);
        this.messageWaitingForResponse = messageWaitingForResponse;

        this.menuItems = menuItems;
        this.listViewWidget = new ListView<>();
        this.defaultValue = defaultValue;
    }

    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        listViewWidget.setItems(FXCollections.observableArrayList(menuItems));
        if (defaultValue != null) {
            int idx = menuItems.indexOf(defaultValue);
            if (idx != -1) {
                listViewWidget.getSelectionModel().select(idx);
                listViewWidget.getFocusModel().focus(idx);
                listViewWidget.scrollTo(idx);
            }
        }
        this.addToContentPane(listViewWidget);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> {
                    MenuItem menuItem = new MenuItem(listViewWidget.getSelectionModel().getSelectedItem(), listViewWidget.getSelectionModel().getSelectedIndex());
                    messageWaitingForResponse.send(menuItem);
                }
        );
    }

}
