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

package org.phoenicis.javafx.views.scriptui;

import javafx.application.Platform;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.javafx.dialogs.ConfirmDialog;
import org.phoenicis.javafx.dialogs.builder.ConfirmDialogBuilder;
import org.phoenicis.scripts.ui.UiQuestionFactory;

@Safe
public class UiQuestionFactoryJavaFX implements UiQuestionFactory {
    private final String wizardTitle;

    public UiQuestionFactoryJavaFX(String title) {
        super();
        this.wizardTitle = title;
    }

    @Override
    public void create(String questionText, Runnable yesCallback, Runnable noCallback) {
        Platform.runLater(() -> {
            final ConfirmDialog confirmMessage = ConfirmDialog.builder()
                    .withTitle(wizardTitle)
                    .withMessage(questionText)
                    .withResizable(true)
                    .withYesCallback(yesCallback)
                    .withNoCallback(noCallback)
                    .build();

            confirmMessage.showAndCallback();
        });
    }
}
