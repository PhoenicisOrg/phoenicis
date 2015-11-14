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

package com.playonlinux.ui.impl.javafx.setupwindow;

import java.util.List;

import com.playonlinux.core.messages.CancelerMessage;
import com.playonlinux.core.messages.CancelerSynchronousMessage;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class StepRepresentationMenu extends StepRepresentationMessage {
    private final List<String> menuItems;
    private final ListView<String> listViewWidget;

    public StepRepresentationMenu(SetupWindowJavaFXImplementation parent, CancelerMessage messageWaitingForResponse,
            String textToShow, List<String> menuItems) {
        super(parent, messageWaitingForResponse, textToShow);

        this.menuItems = menuItems;
        this.listViewWidget = new ListView<>();

    }

    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        listViewWidget.setItems(FXCollections.observableArrayList(menuItems));
        listViewWidget.setLayoutX(10);
        listViewWidget.setLayoutY(40);
        listViewWidget.setPrefSize(700, 328);

        this.addToContentPanel(listViewWidget);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse())
                .setResponse(listViewWidget.getFocusModel().getFocusedItem()));
    }

}
