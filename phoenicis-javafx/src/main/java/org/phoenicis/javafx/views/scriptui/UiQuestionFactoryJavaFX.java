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
import javafx.scene.control.ButtonType;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.javafx.dialogs.MultipleChoiceDialog;
import org.phoenicis.javafx.dialogs.SimpleConfirmDialog;
import org.phoenicis.scripts.ui.UiQuestionFactory;

import java.util.List;

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
            final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
                    .withTitle(wizardTitle)
                    .withMessage(questionText)
                    .withResizable(true)
                    .withYesCallback(yesCallback)
                    .withNoCallback(noCallback)
                    .build();

            confirmMessage.showAndCallback();
        });
    }

    /**
     * Creates a question UI (yes/no decision)
     * <p>
     * Warning: This method needs to be called inside the JavaFX thread, otherwise an exception will be thrown.
     * To ensure that the method will be called inside the JavaFX thread it can be encapsulated
     * inside a <code>Platform.runLater(...)</code> call.
     *
     * @param questionText The question text
     * @return True if the user selects "yes", false if the user selects "no"
     */
    @Override
    public boolean create(String questionText) {
        final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
                .withTitle(wizardTitle)
                .withMessage(questionText)
                .withResizable(true)
                .build();

        final ButtonType result = confirmMessage.showAndWait().orElse(ButtonType.CANCEL);

        return result == ButtonType.OK;
    }

    @Override
    public String create(String questionText, List<String> choices) {
        final MultipleChoiceDialog confirmMessage = MultipleChoiceDialog.builder()
                .withTitle(wizardTitle)
                .withMessage(questionText)
                .withChoiceItems(choices)
                .withResizable(true)
                .build();

        final ButtonType result = confirmMessage.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            return confirmMessage.getSelectedItem();
        } else {
            return null;
        }
    }
}
