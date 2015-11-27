/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.qt.setupwindow;

import com.playonlinux.core.messages.CancelerMessage;
import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.trolltech.qt.gui.*;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * Message step displayed within a SetupWindow
 */
public class TextBoxStep extends AbstractDefaultInstallStep {

    private QLineEdit textBox;

    private final String text;
    private final String defaultValue;

    public TextBoxStep(CancelerMessage message, String text, String defaultValue) {
        super(message);
        this.text = text;
        this.defaultValue = defaultValue;
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        QVBoxLayout contentLayout = new QVBoxLayout(contentPanel);

        QLabel messageLabel = new QLabel(translate(this.text));
        contentLayout.addWidget(messageLabel);

        textBox = new QLineEdit();
        textBox.setText(defaultValue);
        contentLayout.addWidget(textBox);

        contentLayout.addItem(new QSpacerItem(1, 1, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding));
    }


    /* EVENT HANDLERS */

    @Override
    protected void nextButton_clicked() {
        ((CancelerSynchronousMessage) this.getMessage()).setResponse(textBox.text());
    }

}
