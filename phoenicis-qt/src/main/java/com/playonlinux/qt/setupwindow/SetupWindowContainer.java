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

import com.playonlinux.qt.common.ResourceHelper;
import com.playonlinux.ui.api.SetupWindow;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QVBoxLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Qt SetupWindow implementation. This window will contain all running Setups attached into tabs.
 */
public class SetupWindowContainer extends QDialog {

    private QVBoxLayout mainLayout;
    private QTabWidget setupWindowContainer;

    Map<SetupWindow, Integer> setupWindows = new HashMap<>();

    public SetupWindowContainer() {
        setupUi();
        setupWindowContainer.tabCloseRequested.connect(this, "setupWindow_close(int)");
        setupWindowContainer.currentChanged.connect(this, "setupWindow_tabChange(int)");
    }

    private void setupUi() {
        setWindowTitle("PlayOnLinux");
        setStyleSheet(ResourceHelper.getStyleSheet(getClass(), "style.css"));

        mainLayout = new QVBoxLayout(this);
        mainLayout.setMargin(0);
        mainLayout.setSpacing(0);

        setupWindowContainer = new QTabWidget();
        setupWindowContainer.setDocumentMode(true);
        setupWindowContainer.setTabsClosable(true);
        mainLayout.addWidget(setupWindowContainer);

        adjustSize();
        setFixedSize(sizeHint());
    }


    public SetupWindow addSetupWindow(String title) {
        SetupWindowQtImplementation setupWindow = new SetupWindowQtImplementation(this, title);

        int tabPos = setupWindowContainer.addTab(setupWindow, title);
        setupWindows.put(setupWindow.getAdaptor(), tabPos);
        this.show();

        return setupWindow.getAdaptor();
    }

    public void removeSetupWindow(SetupWindow setupWindow) {
        setupWindowContainer.removeTab(setupWindows.remove(setupWindow));
        if (setupWindows.isEmpty()) {
            this.hide();
        }
    }


    /* EVENT HANDLERS */

    private void setupWindow_close(int index) {
        getSetupWindowById(index).getAdaptor().close();
    }

    private void setupWindow_tabChange(int index) {
        setWindowTitle("PlayOnLinux - " + getSetupWindowById(index).getTitle());
    }


    /* HELPERS */

    private SetupWindowQtImplementation getSetupWindowById(int id) {
        SetupWindowQtImplementation setupWindow;
        setupWindow = (SetupWindowQtImplementation) setupWindowContainer.widget(id);
        return setupWindow;
    }

}
