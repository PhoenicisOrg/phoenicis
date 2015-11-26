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
import com.trolltech.qt.gui.*;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * File-Browse step displayed within a SetupWindow
 */
public class BrowseStep extends MessageStep {

    private final File browseDirectory;
    private final List<String> extensionFilters;

    private String selectedFile = null;

    public BrowseStep(CancelerSynchronousMessage message, String text, File browseDirectory, List<String> extensionFilters) {
        super(message, text);
        this.browseDirectory = browseDirectory;
        this.extensionFilters = extensionFilters;
    }

    @Override
    public void setupButtons(QDialogButtonBox buttonBox) {
        super.setupButtons(buttonBox);
        nextButton.setEnabled(false);
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        QWidget browseButtonContainer = new QWidget();
        QHBoxLayout browseButtonContainerLayout = new QHBoxLayout(browseButtonContainer);

        QPushButton browseButton = new QPushButton(translate("Browse"));
        QFont browseButtonFont = new QFont(browseButton.font());
        browseButtonFont.setBold(true);
        browseButtonFont.setPointSize(18);
        browseButton.setFont(browseButtonFont);
        browseButton.setFixedSize(150, 60);

        browseButtonContainerLayout.addItem(new QSpacerItem(1, 1));
        browseButtonContainerLayout.addWidget(browseButton);
        browseButtonContainerLayout.addItem(new QSpacerItem(1, 1));

        contentPanel.layout().addItem(new QSpacerItem(1, 1, QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Expanding));
        contentPanel.layout().addWidget(browseButtonContainer);
        contentPanel.layout().addItem(new QSpacerItem(1, 1, QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Expanding));

        browseButton.clicked.connect(this, "browseButton_clicked()");
    }


    /* EVENT HANDLERS */

    private void browseButton_clicked() {
        QFileDialog fileDialog = new QFileDialog(setupWindow);
        fileDialog.setDirectory(browseDirectory.getPath());
        fileDialog.setFileMode(QFileDialog.FileMode.ExistingFile);
        fileDialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
        String nameFilter = "Allowed Files (*" + StringUtils.join(extensionFilters, " *") + ")";
        fileDialog.setNameFilter(nameFilter);
        fileDialog.setNameFilterDetailsVisible(true);

        if (fileDialog.exec() == QDialog.DialogCode.Accepted.value()) {
            selectedFile = fileDialog.selectedFiles().get(0);
            nextButton.setEnabled(true);
        }
    }

    @Override
    protected void nextButton_clicked() {
        ((CancelerSynchronousMessage) this.getMessage()).setResponse(selectedFile);
    }

}
