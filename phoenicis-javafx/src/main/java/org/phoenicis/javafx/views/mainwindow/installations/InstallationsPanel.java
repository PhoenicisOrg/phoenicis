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

package org.phoenicis.javafx.views.mainwindow.installations;

import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class InstallationsPanel extends DetailsView {
    private final Logger LOGGER = LoggerFactory.getLogger(InstallationsPanel.class);

    public InstallationsPanel() {
        super();
    }

    /*public void setShortcutDTO(ShortcutDTO shortcutDTO) {
        this.setTitle(shortcutDTO.getName());
    
        this.description.setText(shortcutDTO.getDescription());
    
        this.gridPane.getChildren().clear();
    
        try {
            LOGGER.info("Reading shortcut: {}", shortcutDTO.getScript());
    
            final Map<String, Object> shortcutProperties = objectMapper.readValue(shortcutDTO.getScript(),
                    new TypeReference<Map<String, Object>>() {
                    });
    
            int i = 0;
            for (Map.Entry<String, Object> entry : shortcutProperties.entrySet()) {
                final Label keyLabel = new Label(tr(unCamelize(entry.getKey())) + ":");
                keyLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
                GridPane.setValignment(keyLabel, VPos.TOP);
                this.gridPane.add(keyLabel, 0, i);
    
                final Label valueLabel = new Label(entry.getValue().toString());
                valueLabel.setWrapText(true);
                this.gridPane.add(valueLabel, 1, i);
    
                i++;
            }
    
        } catch (IOException e) {
            LOGGER.warn("Could not parse shortcut script JSON", e);
        }
    
        this.runButton.setOnMouseClicked(event -> onShortcutRun.accept(shortcutDTO));
        this.stopButton.setOnMouseClicked(event -> onShortcutStop.accept(shortcutDTO));
        this.uninstallButton.setOnMouseClicked(event -> onShortcutUninstall.accept(shortcutDTO));
    }*/

}
