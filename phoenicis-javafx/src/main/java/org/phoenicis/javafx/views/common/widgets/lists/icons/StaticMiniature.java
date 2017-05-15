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

package org.phoenicis.javafx.views.common.widgets.lists.icons;

import javafx.scene.layout.Region;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A static miniature shown inside a {@link org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListWidget}
 */
public class StaticMiniature extends Region {
    public static URI DEFAULT_MINIATURE;
    public static URI WINE_MINIATURE;
    public static URI CONTAINER_MINIATURE;

    static {
        try {
            DEFAULT_MINIATURE = ListWidget.class.getResource("defaultMiniature.png").toURI();
            WINE_MINIATURE = ListWidget.class.getResource("wineMiniature.png").toURI();
            CONTAINER_MINIATURE = ListWidget.class.getResource("containerMiniature.png").toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor
     *
     * @param miniatureImageUri An uri to the miniature image inside this StaticMiniature
     */
    public StaticMiniature(URI miniatureImageUri) {
        super();

        this.getStyleClass().add("miniatureImage");
        this.setStyle(String.format("-fx-background-image: url(\"%s\");", miniatureImageUri.toString()));
    }
}
