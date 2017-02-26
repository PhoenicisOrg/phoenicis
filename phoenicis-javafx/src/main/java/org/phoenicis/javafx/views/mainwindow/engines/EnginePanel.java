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

package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.phoenicis.engines.dto.WineEngineDTO;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

final class EnginePanel extends VBox {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private final Logger LOGGER = LoggerFactory.getLogger(EnginePanel.class);
    private final WineEngineDTO wineEngineDTO;
    private Node progress;

    private Consumer<WineEngineDTO> onEngineInstall = (engine) -> {};
    private Consumer<WineEngineDTO> onEngineDelete = (engine) -> {};

    public EnginePanel(WineEngineDTO wineEngineDTO) {
        super();
        
        this.wineEngineDTO = wineEngineDTO;

        getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(translate("Version:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        Label name = new Label(wineEngineDTO.getVersion());
        name.setWrapText(true);
        informationContentPane.add(name, 1, 0);

        informationContentPane.add(new TextWithStyle(translate("Mono:"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        Label path = new Label(wineEngineDTO.getMonoFile());
        path.setWrapText(true);
        informationContentPane.add(path, 1, 1);

        informationContentPane.add(new TextWithStyle(translate("Gecko:"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        Label version = new Label(wineEngineDTO.getGeckoFile());
        version.setWrapText(true);
        informationContentPane.add(version, 1, 2);

        informationContentPane.setHgap(20);
        informationContentPane.setVgap(10);

        Button installButton = new Button("Install");
        installButton.setOnMouseClicked(evt -> {
            try {
                onEngineInstall.accept(wineEngineDTO);
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to get engine", e);
                new ErrorMessage("Error while trying to install the engine", e).show();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnMouseClicked(evt -> {
            try {
                onEngineDelete.accept(wineEngineDTO);
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to get engine", e);
                new ErrorMessage("Error while trying to delete the engine", e).show();
            }
        });

        Region spacer = new Region();
        spacer.setPrefHeight(30);
        VBox.setVgrow(spacer, Priority.NEVER);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(installButton, deleteButton);

        Region progressSpacer = new Region();
        progressSpacer.setPrefHeight(30);
        VBox.setVgrow(progressSpacer, Priority.NEVER);

        getChildren().addAll(informationContentPane, spacer, buttonBox, progressSpacer);
    }

    public void showProgress(VBox progressUi) {
        getChildren().remove(progress);
        progress = progressUi;
        getChildren().add(progress);
    }

    public void setOnEngineInstall(Consumer<WineEngineDTO> onEngineInstall) {
        this.onEngineInstall = onEngineInstall;
    }
    public void setOnEngineDelete(Consumer<WineEngineDTO> onEngineDelete) {
        this.onEngineDelete = onEngineDelete;
    }

    public WineEngineDTO getWineEngineDTO() {
        return wineEngineDTO;
    }
}
