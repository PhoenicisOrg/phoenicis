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

import static com.playonlinux.configuration.localisation.Localisation.translate;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;

public class ErrorMessage {
    private final Logger LOGGER = LoggerFactory.getLogger(ErrorMessage.class);
    private final Alert alert;

    public ErrorMessage(String message, Exception exception) {
        LOGGER.error(ExceptionUtils.getStackTrace(exception));
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(translate(message));
        alert.setContentText(String.format("The error was: %s", ExceptionUtils.getStackTrace(exception)));
    }

    public void show() {
        alert.show();
    }
}
