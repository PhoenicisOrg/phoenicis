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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;

/**
 * Item within a menu of the ActionSideBar.
 */
public class ActionMenuItem extends QPushButton {

    public ActionMenuItem(ActionMenu menu, QIcon icon, String text) {
        super(menu);
        setAttribute(Qt.WidgetAttribute.WA_Hover, true);
        setupUi();

        setIcon(icon);
        setText(text);
    }

    private void setupUi() {
        setProperty("class", "ActionMenuItem");
        setIconSize(new QSize(16, 16));
        setFlat(true);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed);
    }

}
