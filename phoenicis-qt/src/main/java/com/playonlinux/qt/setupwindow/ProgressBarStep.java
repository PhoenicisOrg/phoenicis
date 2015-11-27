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
import com.playonlinux.ui.api.ProgressControl;
import com.trolltech.qt.gui.*;

import java.util.List;

/**
 * ProgressBar step displayed within a SetupWindow
 */
public class ProgressBarStep extends MessageStep implements ProgressControl {

    private QProgressBar progressBar;
    private QLabel progressBarLabel;

    public ProgressBarStep(CancelerMessage message, String text) {
        super(message, text);
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        progressBar = new QProgressBar();
        progressBar.setTextVisible(false);
        //calculate with 2 significant figures
        progressBar.setMinimum(0);
        progressBar.setMaximum(100 * 100);
        contentPanel.layout().addWidget(progressBar);

        progressBarLabel = new QLabel();
        contentPanel.layout().addWidget(progressBarLabel);

        contentPanel.layout().addItem(new QSpacerItem(1, 1, QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Expanding));
    }

    @Override
    public void setupButtons(QDialogButtonBox buttonBox) {
        super.setupButtons(buttonBox);
        nextButton.setEnabled(false);
    }


    /* PROGRESS CONTROL */

    @Override
    public void setProgressPercentage(double value) {
        QApplication.invokeLater(() -> progressBar.setValue((int)(value * 100)));
    }

    @Override
    public void setText(String text) {
        QApplication.invokeLater(() -> progressBarLabel.setText(text));
    }
}
