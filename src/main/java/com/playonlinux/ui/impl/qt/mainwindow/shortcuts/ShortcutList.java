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

package com.playonlinux.ui.impl.qt.mainwindow.shortcuts;

import com.playonlinux.ui.impl.qt.mainwindow.MainWindow;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.*;


/**
 * List of installed Shortcuts displayed in the MainWindow.
 */
public class ShortcutList extends QListView {

    public enum IconSize {
        SMALL(new QSize(16,16)),
        MEDIUM(new QSize(24,24)),
        LARGE(new QSize(32,32)),
        VERY_LARGE(new QSize(48,48));

        private QSize size;

        IconSize(QSize size){
            this.size = size;
        }

        public QSize value(){
            return size;
        }

    }

    private final MainWindow mainWindow;


    public ShortcutList(MainWindow mainWindow) {
        super(mainWindow);
        this.mainWindow = mainWindow;

        setupUi();
    }

    private void setupUi() {
        this.setModel(new ShortcutListModel(mainWindow.getEventHandler(), IconSize.VERY_LARGE));
    }

}
