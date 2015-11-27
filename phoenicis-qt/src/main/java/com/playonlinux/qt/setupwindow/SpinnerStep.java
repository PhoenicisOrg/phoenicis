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
import com.playonlinux.qt.common.AnimationHelper;
import com.playonlinux.qt.common.ResourceHelper;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

/**
 * Spinner step displayed within a SetupWindow
 */
public class SpinnerStep extends MessageStep {

    private AnimationHelper loadIndicator;

    public SpinnerStep(CancelerMessage message, String text) {
        super(message, text);
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        QWidget indicatorContainer = new QWidget();
        indicatorContainer.setLayout(new QHBoxLayout());
        QLabel indicatorLabel = new QLabel();
        indicatorContainer.layout().addItem(new QSpacerItem(1,1, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Ignored));
        indicatorContainer.layout().addWidget(indicatorLabel);
        indicatorContainer.layout().addItem(new QSpacerItem(1,1, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Ignored));

        contentPanel.layout().addItem(new QSpacerItem(1,1, QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Expanding));
        contentPanel.layout().addWidget(indicatorContainer);
        contentPanel.layout().addItem(new QSpacerItem(1,1, QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Expanding));

        loadIndicator = new AnimationHelper(indicatorLabel, 85);
        for (int i = 0; i <= 12; ++i) {
            QPixmap frame = ResourceHelper.getPixmap(getClass(), "spinner/" + i + ".png");
            loadIndicator.addFrame(frame);
        }
        loadIndicator.start();
    }

    @Override
    public void tearDown() {
        loadIndicator.stop();
        loadIndicator = null;
    }

    @Override
    public void setupButtons(QDialogButtonBox buttonBox) {
        super.setupButtons(buttonBox);
        nextButton.setEnabled(false);
    }

}
