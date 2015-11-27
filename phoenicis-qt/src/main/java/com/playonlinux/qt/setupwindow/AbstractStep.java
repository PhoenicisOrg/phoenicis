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
import com.playonlinux.qt.common.ResourceHelper;
import com.trolltech.qt.gui.*;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;


/**
 * Abstract base of a step displayed within a SetupWindow
 */
public abstract class AbstractStep {

    protected SetupWindowQtImplementation setupWindow;
    protected QWidget topBar;
    protected QLabel leftImageLabel;

    private QDialogButtonBox buttonBox;
    private CancelerMessage message;


    public enum StepLayout {
        LEFT_IMAGE,
        TOP_BAR,
        NONE
    }

    public enum ButtonType {
        Cancel,
        Next
    }


    public AbstractStep(CancelerMessage message) {
        this.message = message;
    }


    /* GETTERS */

    protected <T extends CancelerMessage> T getMessage() {
        return (T) message;
    }

    public abstract StepLayout getLayout();

    /* RESOURCES */
    public final void setLeftImage(URL leftImage){
        QApplication.invokeLater(() -> {
            QPixmap pixmap = ResourceHelper.getPixmap(leftImage);
            updateLeftImage(pixmap);
        });
    }

    protected void updateLeftImage(QPixmap pixmap){
        leftImageLabel.setPixmap(pixmap);
    }

    /* SETUP */

    public final void setup(SetupWindowQtImplementation setupWindow) {
        this.setupWindow = setupWindow;
    }

    /* TEARDOWN */

    public void tearDown(){}

    /* RENDERING */

    public void setupTopBar(QWidget topBar) {
        this.topBar = topBar;
    }

    public void setupLeftImage(QLabel leftImageLabel) {
        this.leftImageLabel = leftImageLabel;
    }

    public void setupContent(QWidget contentPanel) {
        List<String> classList = new ArrayList<>();
        Class<?> cClass = getClass();
        while (cClass != null) {
            if (AbstractStep.class.isAssignableFrom(cClass) && cClass != AbstractStep.class) {
                classList.add(cClass.getSimpleName());
                cClass = cClass.getSuperclass();
            } else {
                cClass = null;
            }
        }
        String classString = StringUtils.join(classList, " ");
        contentPanel.setProperty("class", contentPanel.property("class") + " " + classString);
    }

    public void setupButtons(QDialogButtonBox buttonBox) {
        this.buttonBox = buttonBox;
    }

    /* HELPERS */

    protected QPushButton addButton(ButtonType buttonType) {
        QPushButton button = null;

        if (buttonType == ButtonType.Cancel) {
            button = buttonBox.addButton(QDialogButtonBox.StandardButton.Cancel);
        } else if (buttonType == ButtonType.Next) {
            button = new QPushButton(QIcon.fromTheme("go-next"), translate("next"));
            buttonBox.addButton(button, QDialogButtonBox.ButtonRole.NoRole);
        }

        return button;
    }

}
