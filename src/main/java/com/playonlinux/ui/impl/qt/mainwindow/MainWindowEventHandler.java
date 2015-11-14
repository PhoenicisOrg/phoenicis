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

package com.playonlinux.ui.impl.qt.mainwindow;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.library.entities.InstalledApplicationEntity;
import com.playonlinux.library.entities.LibraryWindowEntity;
import com.playonlinux.ui.api.EntitiesProvider;
import com.playonlinux.ui.api.UIEventHandler;
import com.playonlinux.ui.events.EventHandler;
import com.playonlinux.ui.impl.qt.mainwindow.shortcuts.ShortcutList;
import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.gui.*;
import org.apache.log4j.Logger;

import java.io.File;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * EventHandler implementation that connects all MainWindow components
 */
@Scan
public class MainWindowEventHandler implements UIEventHandler {
    @Inject
    static EventHandler mainEventHandler;

    private static final Logger LOGGER = Logger.getLogger(MainWindowEventHandler.class);

    private final MainWindow mainWindow;

    private String lastLocalScriptDir = QDir.homePath() + "/";

    public MainWindowEventHandler(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }


    @Override
    public EventHandler getMainEventHandler() {
        return mainEventHandler;
    }



    /* GENERAL */

    /**
     * Initialize the EventHandler
     */
    public void init() {
        //TODO: RESTORE UI-SETTINGS LIKE WINDOW-SIZE, DISPLAY SIZE, ...
        mainWindow.getShortcutMenu().setVisible(false);
    }

    /**
     * Request the application to exit
     *
     * @return False when the user aborted the application exiting, True otherwise.
     */
    public boolean exit() {
        QApplication.exit();
        return true;
    }


    /* GENERAL */
    public void openLink(String url) {
        QDesktopServices.openUrl(new QUrl(url));
    }

    /**
     * Run a local script within the interpreter.
     */
    public void runLocalScript() {
        QFileDialog fileDialog = new QFileDialog(mainWindow);
        fileDialog.setDirectory(lastLocalScriptDir);
        fileDialog.setFileMode(QFileDialog.FileMode.ExistingFile);
        fileDialog.setViewMode(QFileDialog.ViewMode.List);
        fileDialog.setAcceptMode(QFileDialog.AcceptMode.AcceptOpen);
        if (fileDialog.exec() == QDialog.DialogCode.Accepted.value()) {
            File scriptFile = new File(fileDialog.selectedFiles().get(0));

            //save folder to restore next time
            lastLocalScriptDir = scriptFile.getParent();

            try {
                mainEventHandler.runLocalScript(scriptFile);
            } catch (PlayOnLinuxException e) {
                LOGGER.error(e);
                //TODO: DISPLAY ERROR
            }
        }
    }



    /* SHORTCUTS */

    /**
     * Get the EntityProvider for the registered shortcuts.
     *
     * @return EntityProvider containing the registered shortcuts.
     */
    public EntitiesProvider<InstalledApplicationEntity, LibraryWindowEntity> getShortcuts() {
        return mainEventHandler.getInstalledApplications();
    }

    /**
     * Set the iconSize within the ShortcutList
     *
     * @param viewSize Size of the icons displayed within the ShortcutList
     */
    public void setDisplaySize(ShortcutList.IconSize viewSize) {
        mainWindow.getShortcutList().setIconSize(viewSize.value());
    }

    public void shortcutSelected(String title) {
        mainWindow.getShortcutMenu().setTitle(title);
        mainWindow.getShortcutMenu().setVisible(true);
    }

}
