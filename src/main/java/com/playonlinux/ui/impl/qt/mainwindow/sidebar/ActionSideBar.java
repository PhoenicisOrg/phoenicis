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

package com.playonlinux.ui.impl.qt.mainwindow.sidebar;

import static com.playonlinux.core.lang.Localisation.translate;

import java.util.ArrayList;
import java.util.List;

import com.playonlinux.ui.impl.qt.mainwindow.MainWindow;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDockWidget;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;

/**
 * Action SideBar of the MainWindow, containing all possible actions regarding
 * the current context.
 */
public class ActionSideBar extends QDockWidget {

    private QWidget menuList;
    private QVBoxLayout menuListLayout;
    private QSpacerItem menuListSpacer;

    private final List<ActionMenu> subMenus = new ArrayList<>();

    public ActionSideBar(MainWindow mainWindow) {
        super(mainWindow);

        setupUi();
        retranslateUi();
    }

    private void setupUi() {
        setProperty("class", "ActionSideBar");
        setMinimumSize(new QSize(200, 40));
        setFeatures(QDockWidget.DockWidgetFeature.createQFlags(QDockWidget.DockWidgetFeature.DockWidgetFloatable,
                QDockWidget.DockWidgetFeature.DockWidgetMovable));
        setAllowedAreas(Qt.DockWidgetArea.createQFlags(Qt.DockWidgetArea.LeftDockWidgetArea,
                Qt.DockWidgetArea.RightDockWidgetArea));

        menuList = new QWidget();
        menuListLayout = new QVBoxLayout(menuList);
        menuList.setLayout(menuListLayout);
        menuListLayout.setContentsMargins(0, 0, 0, 0);
        menuListLayout.setSpacing(0);
        menuListSpacer = new QSpacerItem(20, 40, QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Expanding);
        menuListLayout.addItem(menuListSpacer);
        setWidget(menuList);
    }

    private void retranslateUi() {
        setWindowTitle(translate("Actions"));
    }

    public ActionMenu addMenu(String title) {
        ActionMenu newMenu = new ActionMenu(this, title);
        addMenu(newMenu);
        return newMenu;
    }

    public void addMenu(ActionMenu menu) {
        subMenus.add(menu);
        menuListLayout.insertWidget((subMenus.size() - 1), menu);
    }

}
