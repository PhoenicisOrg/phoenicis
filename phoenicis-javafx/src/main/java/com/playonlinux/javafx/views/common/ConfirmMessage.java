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

package com.playonlinux.javafx.views.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ConfirmMessage {
    private final Logger LOGGER = LoggerFactory.getLogger(ConfirmMessage.class);
    private final Alert alert;

    public ConfirmMessage(String title, String message) {
        LOGGER.info("Ask for confirmation", message);
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
    }

    public void ask(Runnable yesCallback) {
        ask(yesCallback, () -> {});
    }

    public void ask(Runnable yesCallback, Runnable noCallback) {
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            LOGGER.info("User response: yes");
            yesCallback.run();
        } else {
            LOGGER.info("User response: no");
            noCallback.run();
        }
    }
}
