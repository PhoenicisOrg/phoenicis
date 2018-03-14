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

import org.phoenicis.scripts.ui.Message;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

abstract class AbstractStepRepresentationWithHeader extends AbstractStepRepresentation {
    private Pane contentPane;

    AbstractStepRepresentationWithHeader(SetupUiJavaFXImplementation parent, Message<?> messageWaitingForResponse) {
        super(parent, messageWaitingForResponse);

        this.contentPane = new VBox();
        this.contentPane.setId("panelForTopheader");
        getParent().getRoot().setCenter(this.contentPane);
    }

    public Pane getContentPane() {
        return contentPane;
    }

    public void addToContentPane(Node contentToAdd) {
        this.contentPane.getChildren().add(contentToAdd);
        getParent().getRoot().setCenter(this.contentPane);
    }

    /**
     * Draw the header at the top of the window
     */
    private void drawHeader() {
        Pane header = new Pane();
        header.setId("header");
        header.setPrefSize(722, 65);
        header.setLayoutX(-1);
        header.setLayoutY(-1);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView topImage = new ImageView(this.createTopImage());
        topImage.setLayoutX(626);

        header.getChildren().add(topImage);
        getParent().getRoot().setTop(header);
    }

    private Image createTopImage() {
        return new Image(this.getParentTopImage().toString());
    }

    public void installStep() {
        this.clearAllOnParent();
        this.drawHeader();
        this.drawFooter();

        this.setStepEvents();
        this.drawStepContent();
    }

}
