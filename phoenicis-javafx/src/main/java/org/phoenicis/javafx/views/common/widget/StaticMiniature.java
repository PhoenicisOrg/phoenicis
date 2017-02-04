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

package org.phoenicis.javafx.views.common.widget;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StaticMiniature extends ImageView {
    public static final Image DEFAULT_MINIATURE =
            new Image(MiniatureListWidget.class.getResource("defaultMiniature.png").toExternalForm());

    public static final Image WINE_MINIATURE =
            new Image(MiniatureListWidget.class.getResource("wineMiniature.png").toExternalForm());

    public StaticMiniature(Image defaultImage) {
        super(defaultImage);
        this.setFitWidth(120);
        this.setFitHeight(90);
        this.getStyleClass().add("miniatureImage");
    }

    public StaticMiniature() {
        this(DEFAULT_MINIATURE);
    }
}
