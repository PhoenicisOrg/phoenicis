/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.qt.mainwindow;

import static com.playonlinux.core.lang.Localisation.translate;

import com.playonlinux.qt.common.ResourceHelper;
import com.playonlinux.qt.mainwindow.menubar.MenuBar;
import com.playonlinux.qt.mainwindow.shortcuts.ShortcutList;
import com.playonlinux.qt.mainwindow.sidebar.ActionMenu;
import com.playonlinux.qt.mainwindow.sidebar.ActionSideBar;
import com.playonlinux.qt.mainwindow.toolbar.ToolBar;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QWidget;

/**
 * MainWindow of PlayOnLinux's Qt-Gui implementation.
 */
public class MainWindow extends QMainWindow implements PlayOnLinuxWindow {

    private final MainWindowEventHandler eventHandler;

    private MenuBar menuBar;
    private ToolBar toolBar;

    private ActionSideBar actionSideBar;
    private ActionMenu polMenu;
    private ShortcutSidebarMenu shortcutMenu;

    private QWidget centralwidget;
    private QHBoxLayout mainLayout;

    private ShortcutList shortcutList;


    public MainWindow() {
        eventHandler = new MainWindowEventHandler(this);

        setupUi();
        retranslateUi();

        eventHandler.init();
        this.show();
    }


    private void setupUi() {
        setStyleSheet(ResourceHelper.getStyleSheet(getClass(), "style.css"));

        menuBar = new MenuBar(this);
        this.setMenuBar(menuBar);

        toolBar = new ToolBar(this);
        addToolBar(Qt.ToolBarArea.TopToolBarArea, toolBar);

        actionSideBar = new ActionSideBar(this);
        addDockWidget(Qt.DockWidgetArea.LeftDockWidgetArea, actionSideBar);
        polMenu = new StaticSidebarMenu(this);
        shortcutMenu = new ShortcutSidebarMenu(this);
        actionSideBar.addMenu(polMenu);
        actionSideBar.addMenu(shortcutMenu);

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
    protected MenuBar getMenuBar() {
        return menuBar;
    }

    protected ToolBar getToolBar() {
        return toolBar;
    }

    protected ActionSideBar getSideBar() {
        return actionSideBar;
    }

    protected ShortcutSidebarMenu getShortcutMenu() {
        return shortcutMenu;
    }

    protected ShortcutList getShortcutList() {
        return shortcutList;
    }




    /* EVENTS */

    @Override
    protected void closeEvent(QCloseEvent e) {
        eventHandler.exit();
        e.ignore();
    }

}
