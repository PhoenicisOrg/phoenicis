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

package com.playonlinux.ui.impl.javafx.common.widget;

import static com.playonlinux.core.lang.Localisation.translate;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PlayOnLinuxLogo extends HBox {
    public PlayOnLinuxLogo() {
        super();
        Image logoImage = new Image(this.getClass().getResourceAsStream("/playonlinux.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(28);
        logoView.setFitHeight(28);

        Text text = new Text(translate("${application.name}").toLowerCase());
        text.setId("logoText");

        this.getChildren().add(logoView);
        this.getChildren().add(text);

    }
}
