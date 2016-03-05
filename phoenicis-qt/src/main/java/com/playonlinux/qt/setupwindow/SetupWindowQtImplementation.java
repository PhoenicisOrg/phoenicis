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

import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Qt-Implementation for Setupwindows
 * This is not SetupWindow-Interface compatible, the SetupWindowAdaptor is used for that
 */
public class SetupWindowQtImplementation extends QWidget {

    private QVBoxLayout windowLayout;
    private QWidget container;
    private QGridLayout containerLayout;
    private QDialogButtonBox buttonBox;

    private QLabel leftImage;
    private QWidget topBar;
    private QWidget content;

    @Getter(AccessLevel.PROTECTED)
    private final SetupWindowContainer windowContainer;
    @Getter(AccessLevel.PROTECTED)
    private AbstractStep step = null;
    private SetupWindowAdaptor adaptor = null;

    @Getter(AccessLevel.PROTECTED)
    private String title;
    
    
    public SetupWindowQtImplementation(SetupWindowContainer container, String title) {
        this.windowContainer = container;
        this.title = title;
        setupUi();
    }



    /* UI SETUP */

    private void setupUi() {
        this.setProperty("class", "SetupWindow");

        windowLayout = new QVBoxLayout(this);
        windowLayout.setMargin(0);
        windowLayout.setSpacing(4);

        container = new QWidget();
        container.setFixedWidth(520);
        container.setFixedHeight(356);
        containerLayout = new QGridLayout(container);

        buttonBox = new QDialogButtonBox();

        windowLayout.addWidget(container);
        windowLayout.addWidget(buttonBox);
    }

    private void resetUi() {
        QLayoutItemInterface item;
        while ((item = containerLayout.takeAt(0)) != null) {
            containerLayout.removeWidget(item.widget());
            item.widget().dispose();
        }
        containerLayout.dispose();
        containerLayout = new QGridLayout(container);
        containerLayout.setSpacing(0);
        containerLayout.setMargin(0);

        windowLayout.removeWidget(buttonBox);
        buttonBox.dispose();
        buttonBox = new QDialogButtonBox();
        windowLayout.addWidget(buttonBox);
    }

    private void setupUi(AbstractStep.StepLayout stepLayout) {
        resetUi();

        content = new QWidget();
        content.setProperty("class", "Content");
        content.setContentsMargins(10, 10, 10, 10);

        if (stepLayout == AbstractStep.StepLayout.LEFT_IMAGE) {
            leftImage = new QLabel();
            leftImage.setProperty("class", "LeftImage");

            containerLayout.addWidget(leftImage, 0, 0, 1, 1);
            containerLayout.addWidget(content, 0, 1, 1, 1);
        } else if (stepLayout == AbstractStep.StepLayout.TOP_BAR) {
            topBar = new QWidget();
            topBar.setProperty("class", "TopBar");

            containerLayout.addWidget(topBar, 0, 0, 1, 1);
            containerLayout.addWidget(content, 1, 0, 1, 1);
        } else {
            containerLayout.addWidget(content, 0, 0, 1, 1);
        }
    }

    protected void setStep(AbstractStep step) {
        if (this.step != null) {
            this.step.tearDown();
        }

        step.setup(this);
        setupUi(step.getLayout());

        if (step.getLayout() == AbstractStep.StepLayout.LEFT_IMAGE) {
            step.setupLeftImage(leftImage);
        } else if (step.getLayout() == AbstractStep.StepLayout.TOP_BAR) {
            step.setupTopBar(topBar);
        }

        step.setupButtons(buttonBox);
        step.setupContent(content);

        this.step = step;
    }

    protected SetupWindowAdaptor getAdaptor() {
        if (adaptor == null) {
            adaptor = new SetupWindowAdaptor(this);
        }
        return adaptor;
    }

}
