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

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import com.playonlinux.utils.messages.CancelerMessage;

abstract class AbstractStepRepresentationWithHeader extends AbstractStepRepresentation {
    Pane contentPanel;

    AbstractStepRepresentationWithHeader(SetupWindowJavaFXImplementation parent, CancelerMessage messageWaitingForResponse) {
        super(parent, messageWaitingForResponse);
    }

    public Pane getContentPanel() {
        return contentPanel;
    }

    public void addToContentPanel(Node contentToAdd) {
        this.contentPanel.getChildren().add(contentToAdd);
    }
    /**
     * Draw the header at the top of the window
     */
    private void drawHeader() {
        String title = this.getParentWizardTitle(); // FIXME: use this variable to draw the title of the window
        Pane header = new Pane();
        header.setId("header");
        header.setPrefSize(522, 65);
        header.setLayoutX(-1);
        header.setLayoutY(-1);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView topImage = new ImageView(this.createTopImage());
        topImage.setLayoutX(426);

        header.getChildren().add(topImage);
        this.addToStep(header);
    }

    private void drawPanelForTopHeader() {
        Pane panel = new Pane();
        panel.setId("panelForTopheader");
        panel.setPrefSize(522, 294);
        panel.setLayoutX(-1);
        panel.setLayoutY(63);
        this.addToStep(panel);
        this.contentPanel = panel;
    }

    private Image createTopImage() {
        return new Image(this.getParentTopImage().toString());
    }

    public void installStep() {
        this.clearAllOnParent();
        this.drawHeader();
        this.drawPanelForTopHeader();
        this.drawFooter();

        this.setStepEvents();
        this.drawStepContent();
    }

}
