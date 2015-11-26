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
import com.playonlinux.core.utils.OperatingSystem;
import com.playonlinux.qt.common.ResourceHelper;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

/**
 * Step used as base for all presenting steps for code-sharing
 * This base provides the default next & cancel - buttons and the default leftImage
 */
public abstract class AbstractDefaultPresentationStep extends AbstractDefaultStep {

    public AbstractDefaultPresentationStep(CancelerMessage message) {
        super(message);
    }

    @Override
    public StepLayout getLayout() {
        return StepLayout.LEFT_IMAGE;
    }

    @Override
    public void setupLeftImage(QLabel leftImage) {
        switch (OperatingSystem.fetchCurrentOperationSystem()) {
            case LINUX:
                leftImage.setPixmap(ResourceHelper.getPixmap(getClass(), "playonlinux.png"));
                break;
            case MACOSX:
                leftImage.setPixmap(ResourceHelper.getPixmap(getClass(), "playonmac.png"));
                break;
        }
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);
        contentPanel.setProperty("class", contentPanel.property("class") + " presentation");
    }

}
