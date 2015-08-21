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

import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.qt.mainwindow.menubar.MenuBar;
import com.playonlinux.ui.impl.qt.mainwindow.shortcuts.ShortcutList;
import com.playonlinux.ui.impl.qt.mainwindow.sidebar.ActionSideBar;
import com.playonlinux.ui.impl.qt.mainwindow.toolbar.ToolBar;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import org.gnome.gtk.Toolbar;

import java.awt.*;

import static com.playonlinux.core.lang.Localisation.translate;

/**
 * MainWindow of PlayOnLinux's Qt-Gui implementation.
 */
public class MainWindow extends QMainWindow implements PlayOnLinuxWindow {

    private final MainWindowEventHandler eventHandler;

    private MenuBar menuBar;
    private ToolBar toolBar;
    private ActionSideBar actionSideBar;

    private QWidget centralwidget;
    private QHBoxLayout mainLayout;

    private ShortcutList shortcutList;


    public MainWindow() {
        eventHandler = new MainWindowEventHandler(this);

        setupUi();
        retranslateUi();

        this.show();
    }


    private void setupUi() {
        menuBar = new MenuBar(this);
        this.setMenuBar(menuBar);

        toolBar = new ToolBar(this);
        addToolBar(Qt.ToolBarArea.TopToolBarArea, toolBar);

        actionSideBar = new ActionSideBar(this);
        addDockWidget(Qt.DockWidgetArea.LeftDockWidgetArea, actionSideBar);

        centralwidget = new QWidget(this);
        setCentralWidget(centralwidget);
        mainLayout = new QHBoxLayout(centralwidget);
        mainLayout.setMargin(0);

        shortcutList = new ShortcutList(this);
        mainLayout.addWidget(shortcutList);

        resize(new QSize(800, 600).expandedTo(minimumSizeHint()));
    }

    private void retranslateUi() {
        setWindowTitle(translate("PlayOnLinux"));
    }


    public MainWindowEventHandler getEventHandler() {
        return eventHandler;
    }



    /* COMPONENT GETTERS */
    //grant access to ui components for the MainWindowEventHandler within the mainwindow namespace only
    protected MenuBar getMenuBar(){ return menuBar; }
    protected ToolBar getToolBar(){ return toolBar; }
    protected ActionSideBar getSideBar(){ return actionSideBar; }
    protected ShortcutList getShortcutList(){ return shortcutList; }




    /* EVENTS */

    @Override
    protected void closeEvent(QCloseEvent e) {
        eventHandler.exit();
        e.ignore();
    }

}
