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

import com.playonlinux.core.messages.CancelerSynchronousMessage;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

/**
 * Presentation step displayed within a SetupWindow
 */
public class PresentationStep extends AbstractDefaultPresentationStep {

    private final String text;

    public PresentationStep(CancelerSynchronousMessage message, String text) {
        super(message);
        this.text = text;
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        QVBoxLayout contentLayout = new QVBoxLayout(contentPanel);
        contentLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));
        contentLayout.setSpacing(30);

        QLabel titleLabel = new QLabel(setupWindow.getTitle());
        QFont titleFont = new QFont(titleLabel.font());
        titleFont.setBold(true);
        titleFont.setPointSize(titleFont.pointSize() + 2);
        titleLabel.setFont(titleFont);
        contentLayout.addWidget(titleLabel);

        QLabel textLabel = new QLabel(this.text);
        contentLayout.addWidget(textLabel);
    }

}