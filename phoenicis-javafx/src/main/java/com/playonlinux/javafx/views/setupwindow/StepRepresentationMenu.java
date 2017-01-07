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

package com.playonlinux.javafx.views.setupwindow;

import com.playonlinux.scripts.ui.Message;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.List;


public class StepRepresentationMenu extends StepRepresentationMessage {
    private final List<String> menuItems;
    private final ListView<String> listViewWidget;
    private final Message<String> messageWaitingForResponse;

    public StepRepresentationMenu(SetupWindowJavaFXImplementation parent, Message<String> messageWaitingForResponse, String textToShow, List<String> menuItems) {
        super(parent, messageWaitingForResponse, textToShow);
        this.messageWaitingForResponse = messageWaitingForResponse;

        this.menuItems = menuItems;
        this.listViewWidget = new ListView<>();

    }

    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        listViewWidget.setItems(FXCollections.observableArrayList(menuItems));
        this.addToContentPane(listViewWidget);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
                        messageWaitingForResponse.send(listViewWidget.getFocusModel().getFocusedItem())
        );
    }

}
