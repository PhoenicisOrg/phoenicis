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

package com.playonlinux.ui.impl.javafx.mainwindow.apps;

import com.playonlinux.dto.ui.AppsItemDTO;
import com.playonlinux.ui.impl.javafx.common.HtmlTemplate;
import com.playonlinux.ui.impl.javafx.widget.Title;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.apache.log4j.Logger;

import java.io.IOException;

final class AppPanel extends VBox {
    private final Logger logger = Logger.getLogger(AppPanel.class);

    public AppPanel(AppsItemDTO appsItemDTO) {
        super();
        this.getStyleClass().addAll("rightPane", "appPresentation");

        final WebView descriptionWidget = new WebView();

        try {
            StringBuilder images = new StringBuilder();
            for(String imageUrl: appsItemDTO.getMiniaturesUrls()) {
                images.append(String.format("<img src='%s' /> ", imageUrl, imageUrl));
            }

            descriptionWidget.getEngine().loadContent(
                    new HtmlTemplate(this.getClass().getResource("descriptionTemplate.html"))
                            .render(
                                    appsItemDTO.getDescription(),
                                    images.toString()
                            )
            );
        } catch (IOException e) {
            logger.error("Unable to load the description");
        }

        this.getChildren().add(new Title(appsItemDTO.getName()));
        this.getChildren().add(descriptionWidget);


    }
}
