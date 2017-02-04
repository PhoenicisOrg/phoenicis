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

package org.phoenicis.javafx.views.mainwindow.apps;

import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.ScriptDTO;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.HtmlTemplate;
import com.sun.webkit.dom.HTMLAnchorElementImpl;
import javafx.concurrent.Worker;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;

final class AppPanel extends VBox {
    private final Logger LOGGER = LoggerFactory.getLogger(AppPanel.class);
    private final ApplicationDTO applicationDTO;
    private final String themeName;

    public void setOnScriptInstall(Consumer<ScriptDTO> onScriptInstall) {
        this.onScriptInstall = onScriptInstall;
    }

    private Consumer<ScriptDTO> onScriptInstall = (script) -> {};

    public AppPanel(ApplicationDTO applicationDTO, String themeName) {
        super();
        this.applicationDTO = applicationDTO;
        this.themeName = themeName;
        this.getStyleClass().addAll("rightPane", "appPresentation");

        final WebView descriptionWidget = new WebView();

        try {
            descriptionWidget.getEngine().loadContent(
                    new HtmlTemplate(
                            this.getClass().getResourceAsStream("/org/phoenicis/javafx/themes/"+themeName+"/descriptionTemplate.html")
                    ).render(applicationDTO)
            );
        } catch (IOException e) {
            LOGGER.error("Unable to load the description", e);
        }

        descriptionWidget.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                EventListener listener = ev -> {
                    if (ev.getTarget() instanceof HTMLAnchorElementImpl) {
                        final String linkHrefAttribute = ((HTMLAnchorElementImpl) ev.getTarget()).getHref();

                        try {
                            onScriptInstall.accept(fetchScriptFromName(linkHrefAttribute));
                        } catch (IllegalArgumentException e) {
                            LOGGER.error("Failed to get script", e);
                            new ErrorMessage("Error while trying to download the installer", e).show();
                        }
                    }
                };

                final Document doc = descriptionWidget.getEngine().getDocument();
                final NodeList lista = doc.getElementsByTagName("a");

                for (int i = 0; i < lista.getLength(); i++) {
                    ((EventTarget) lista.item(i)).addEventListener("click", listener, false);
                }
            }
        });


        final HBox miniaturesPane = new HBox();
        miniaturesPane.getStyleClass().add("appPanelMiniaturesPane");

        final ScrollPane miniaturesPaneWrapper = new ScrollPane(miniaturesPane);
        miniaturesPaneWrapper.getStyleClass().add("appPanelMiniaturesPaneWrapper");

        for (byte[] miniatureBytes : applicationDTO.getMiniatures()) {
            Image image = new Image(new ByteArrayInputStream(miniatureBytes));
            ImageView imageView = new ImageView(image);
            imageView.fitHeightProperty().bind(miniaturesPaneWrapper.heightProperty().multiply(0.8));
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            miniaturesPane.getChildren().add(imageView);
        }

        this.getChildren().add(descriptionWidget);
        this.getChildren().add(miniaturesPaneWrapper);
    }

    private ScriptDTO fetchScriptFromName(String scriptName) {
        for (ScriptDTO scriptDTO : applicationDTO.getScripts()) {
            if(scriptName.equals(scriptDTO.getName())) {
                return scriptDTO;
            }
        }

        return null;
    }
}
