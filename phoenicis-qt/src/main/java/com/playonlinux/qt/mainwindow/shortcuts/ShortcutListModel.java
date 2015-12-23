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

package com.playonlinux.qt.mainwindow.shortcuts;

import java.util.ArrayList;
import java.util.List;

import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.qt.mainwindow.MainWindowEventHandler;
import com.playonlinux.ui.api.EntitiesProvider;
import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPixmap;

/**
 * Model for the ShortcutList, automatically keeping track of changes.
 */
public class ShortcutListModel extends QAbstractListModel {

    private List<InstalledApplicationEntity> libraryItems = new ArrayList<>();
    private final EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> libraryItemProvider;
    private final QSize maxIconSize;


    public ShortcutListModel(MainWindowEventHandler eventHandler, ShortcutList.IconSize maxIconSize) {
        this.maxIconSize = maxIconSize.value();

        libraryItemProvider = eventHandler.getShortcuts();
        libraryItemProvider.setOnChange(this::update);
    }


    @Override
    public Object data(QModelIndex index, int role) {
        if (index.row() < rowCount()) {
            if (role == Qt.ItemDataRole.DecorationRole) {
                String imageFile = libraryItems.get(index.row()).getIcon().getFile();
                //return QIcon that is fed by a QPixmap with the image @ the maximal displaySize
                //QIcon then handles the sizing by  setIconSize()
                return new QIcon(new QPixmap(imageFile).scaled(maxIconSize));
            } else if (role == Qt.ItemDataRole.DisplayRole) {
                return libraryItems.get(index.row()).getName();
            }
        }

        return null;
    }

    @Override
    public int rowCount(QModelIndex qModelIndex) {
        return libraryItems.size();
    }


    //Register for changes and notify the view.
    public void update(LibraryWindowEntity shortcuts) {
        QApplication.invokeLater(() -> {
            layoutAboutToBeChanged.emit();
            libraryItems = new ArrayList<>(shortcuts.getInstalledApplicationEntity());
            layoutChanged.emit();
        });
    }

}
