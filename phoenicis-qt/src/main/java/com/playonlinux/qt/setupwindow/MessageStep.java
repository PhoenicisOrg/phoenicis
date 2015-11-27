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
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

/**
 * Message step displayed within a SetupWindow
 */
public class MessageStep extends AbstractDefaultInstallStep {

    private final String text;

    protected QLabel messageLabel;

    public MessageStep(CancelerMessage message, String text) {
        super(message);
        this.text = text;
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        QVBoxLayout contentLayout = new QVBoxLayout();
        contentLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        contentPanel.setLayout(contentLayout);

        messageLabel = new QLabel(text);
        messageLabel.setWordWrap(true);
        contentLayout.addWidget(messageLabel);
    }

}
