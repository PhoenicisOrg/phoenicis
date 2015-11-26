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
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

/**
 * Step used as base for all installing steps for code-sharing
 * This base provides the default next & cancel - buttons and a topBar
 */
public abstract class AbstractDefaultInstallStep extends AbstractDefaultStep {

    public AbstractDefaultInstallStep(CancelerMessage message) {
        super(message);
    }

    @Override
    public StepLayout getLayout() {
        return StepLayout.TOP_BAR;
    }

    @Override
    public void setupTopBar(QWidget topBarPanel) {
        QVBoxLayout topLayout = new QVBoxLayout(topBarPanel);

        QLabel titleLabel = new QLabel(setupWindow.getTitle());
        QFont titleFont = new QFont(titleLabel.font());
        titleFont.setBold(true);
        titleFont.setPointSize(titleFont.pointSize() + 2);
        titleLabel.setFont(titleFont);
        topLayout.addWidget(titleLabel);

        QLabel subTitleLabel = new QLabel();
        topLayout.addWidget(subTitleLabel);
    }

}