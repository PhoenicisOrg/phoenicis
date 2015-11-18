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

package com.playonlinux.qt.mainwindow.sidebar;

import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QVBoxLayout;

/**
 * Menu displayed within the ActionSideBar.
 */
public class ActionMenu extends QGroupBox {

    QVBoxLayout menuLayout;

    public ActionMenu(ActionSideBar sideBar, String title) {
        super(sideBar);
        setupUi();
        setTitle(title);
    }

    private void setupUi() {
        setProperty("class", "ActionMenu");
        menuLayout = new QVBoxLayout(this);
        setLayout(menuLayout);
        menuLayout.setSpacing(2);
        menuLayout.setContentsMargins(12, 0, 6, 12);
        setFlat(true);
    }

    public ActionMenuItem addMenuItem(QIcon icon, String title) {
        ActionMenuItem newMenuItem = new ActionMenuItem(this, icon, title);
        menuLayout.addWidget(newMenuItem);
        return newMenuItem;
    }

}
