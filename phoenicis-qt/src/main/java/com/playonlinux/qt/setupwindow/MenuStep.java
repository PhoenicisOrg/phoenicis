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
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QWidget;

import java.util.List;

/**
 * Menu step displayed within a SetupWindow
 */
public class MenuStep extends MessageStep {

    private List<String> menuItems;

    protected QListWidget menuList;

    public MenuStep(CancelerSynchronousMessage message, String text, List<String> menuItems) {
        super(message, text);
        this.menuItems = menuItems;
    }

    @Override
    public void setupContent(QWidget contentPanel) {
        super.setupContent(contentPanel);

        menuList = new QListWidget();
        QFont menuListFont = new QFont(menuList.font());
        menuListFont.setPixelSize(15);
        menuList.setFont(menuListFont);

        menuList.addItems(menuItems);
        menuList.setCurrentRow(0);

        contentPanel.layout().addWidget(menuList);
    }

    @Override
    protected void nextButton_clicked() {
        QListWidgetItem item = menuList.currentItem();
        String itemStr = (item != null) ? item.text() : null;
        ((CancelerSynchronousMessage) this.getMessage()).setResponse(itemStr);
    }

}
