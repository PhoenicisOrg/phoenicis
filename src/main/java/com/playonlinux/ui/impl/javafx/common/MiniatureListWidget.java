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

package com.playonlinux.ui.impl.javafx.common;

import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public final class MiniatureListWidget extends ScrollPane {
    private final Pane content;

    public static MiniatureListWidget create() {
        FlowPane content = new FlowPane();
        return new MiniatureListWidget(content);
    }

    private Image defaultMiniature =
            new Image(MiniatureListWidget.class.getResource("defaultMiniature.png").toExternalForm());

    private MiniatureListWidget(Pane content) {
        super(content);

        this.content = content;
        this.getStyleClass().add("rightPane");

        this.content.getStyleClass().addAll("miniatureList");

        this.getChildren().add(this.content);

        this.setCache(true);
        this.setCacheHint(CacheHint.QUALITY);
    }

    public void clear() {
        content.getChildren().clear();
    }

    public void addItem(String name) {
        content.getChildren().add(new Element(name));
    }


    private class Element extends VBox {
        private final String name;

        Element(String name) {
            this.getStyleClass().add("miniatureListElement");

            this.setAlignment(Pos.CENTER);
            this.name = name;

            ImageView miniature = new ImageView(defaultMiniature);
            miniature.setFitWidth(120);
            miniature.setFitHeight(90);
            miniature.getStyleClass().add("miniatureImage");
            Text label = new Text(name);
            label.setWrappingWidth(150);
            label.getStyleClass().add("miniatureText");

            this.getChildren().add(miniature);
            this.getChildren().add(label);

        }

        String getName() {
            return name;
        }
    }
}
