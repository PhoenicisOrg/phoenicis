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

/********************************************************************************
 * * Form generated from reading ui file 'mainWindow.jui'
 * *
 * * Created by: Qt User Interface Compiler version 4.8.7
 ********************************************************************************/

import com.playonlinux.ui.impl.qt.common.IconHelper;
import com.trolltech.qt.QUiForm;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import static com.playonlinux.core.lang.Localisation.translate;

public class UI_MainWindow implements QUiForm<QMainWindow> {
    public QAction actionRun;
    public QAction actionInstall;
    public QAction actionRemove;
    public QAction actionDonate;
    public QAction actionExit;
    public QAction actionSmall_Icons;
    public QAction actionMedium_Icons;
    public QAction actionLarge_Icons;
    public QAction actionVery_Large_Icons;
    public QAction actionWineVersions;
    public QAction actionLocalScript;
    public QAction actionConsole;
    public QAction actionCloseAll;
    public QAction actionDebugger;
    public QAction actionNetwork;
    public QAction actionAbout;
    public QAction actionSoftware;
    public QAction actionNews;
    public QAction actionForums;
    public QAction actionBugs;
    public QAction actionGooglePlus;
    public QAction actionTwitter;
    public QAction actionFacebook;
    public QAction actionToolRun;
    public QAction actionToolClose;
    public QAction actionToolInstall;
    public QAction actionToolRemove;
    public QAction actionToolConfigure;
    public QWidget centralwidget;
    public QHBoxLayout horizontalLayout;
    public QTreeView treeView;
    public QDockWidget actionsSideBar;
    public QWidget actionsSideBarContent;
    public QMenuBar menuBar;
    public QMenu menuFile;
    public QMenu menuDisplay;
    public QMenu menuTools;
    public QMenu menuSettings;
    public QMenu menuHelp;
    public QMenu menuContact;
    public QToolBar toolBar;

    public UI_MainWindow() {
        super();
    }

    public void setupUi(QMainWindow MainWindow) {
        MainWindow.setObjectName("MainWindow");
        MainWindow.resize(new QSize(800, 600).expandedTo(MainWindow.minimumSizeHint()));
        //TODO: WindowIcon
        actionRun = new QAction(MainWindow);
        actionRun.setObjectName("actionRun");
        actionRun.setIcon(QIcon.fromTheme("document-open"));
        actionInstall = new QAction(MainWindow);
        actionInstall.setObjectName("actionInstall");
        actionInstall.setIcon(QIcon.fromTheme("list-add"));
        actionRemove = new QAction(MainWindow);
        actionRemove.setObjectName("actionRemove");
        actionRemove.setIcon(QIcon.fromTheme("edit-delete"));
        actionDonate = new QAction(MainWindow);
        actionDonate.setObjectName("actionDonate");
        actionExit = new QAction(MainWindow);
        actionExit.setObjectName("actionExit");
        actionExit.setIcon(QIcon.fromTheme("application-exit"));
        actionSmall_Icons = new QAction(MainWindow);
        actionSmall_Icons.setObjectName("actionSmall_Icons");
        actionSmall_Icons.setCheckable(true);
        actionMedium_Icons = new QAction(MainWindow);
        actionMedium_Icons.setObjectName("actionMedium_Icons");
        actionMedium_Icons.setCheckable(true);
        actionLarge_Icons = new QAction(MainWindow);
        actionLarge_Icons.setObjectName("actionLarge_Icons");
        actionLarge_Icons.setCheckable(true);
        actionVery_Large_Icons = new QAction(MainWindow);
        actionVery_Large_Icons.setObjectName("actionVery_Large_Icons");
        actionVery_Large_Icons.setCheckable(true);
        actionWineVersions = new QAction(MainWindow);
        actionWineVersions.setObjectName("actionWineVersions");
        actionLocalScript = new QAction(MainWindow);
        actionLocalScript.setObjectName("actionLocalScript");
        actionConsole = new QAction(MainWindow);
        actionConsole.setObjectName("actionConsole");
        actionCloseAll = new QAction(MainWindow);
        actionCloseAll.setObjectName("actionCloseAll");
        actionDebugger = new QAction(MainWindow);
        actionDebugger.setObjectName("actionDebugger");
        actionNetwork = new QAction(MainWindow);
        actionNetwork.setObjectName("actionNetwork");
        actionNetwork.setIcon(QIcon.fromTheme("network-wired"));
        actionAbout = new QAction(MainWindow);
        actionAbout.setObjectName("actionAbout");
        actionAbout.setIcon(QIcon.fromTheme("help-about"));
        actionSoftware = new QAction(MainWindow);
        actionSoftware.setObjectName("actionSoftware");
        actionNews = new QAction(MainWindow);
        actionNews.setObjectName("actionNews");
        actionForums = new QAction(MainWindow);
        actionForums.setObjectName("actionForums");
        actionBugs = new QAction(MainWindow);
        actionBugs.setObjectName("actionBugs");
        actionGooglePlus = new QAction(MainWindow);
        actionGooglePlus.setObjectName("actionGooglePlus");
        actionTwitter = new QAction(MainWindow);
        actionTwitter.setObjectName("actionTwitter");
        actionFacebook = new QAction(MainWindow);
        actionFacebook.setObjectName("actionFacebook");
        actionToolRun = new QAction(MainWindow);
        actionToolRun.setObjectName("actionToolRun");
        actionToolRun.setCheckable(false);
        actionToolRun.setChecked(false);
        actionToolRun.setIcon(IconHelper.fromResource(getClass(), "toolBar/play.png"));
        actionToolRun.setIconVisibleInMenu(true);
        actionToolClose = new QAction(MainWindow);
        actionToolClose.setObjectName("actionToolClose");
        actionToolClose.setIcon(IconHelper.fromResource(getClass(), "toolBar/stop.png"));
        actionToolInstall = new QAction(MainWindow);
        actionToolInstall.setObjectName("actionToolInstall");
        actionToolInstall.setIcon(IconHelper.fromResource(getClass(), "toolBar/install.png"));
        actionToolRemove = new QAction(MainWindow);
        actionToolRemove.setObjectName("actionToolRemove");
        actionToolRemove.setIcon(IconHelper.fromResource(getClass(), "toolBar/delete.png"));
        actionToolConfigure = new QAction(MainWindow);
        actionToolConfigure.setObjectName("actionToolConfigure");
        actionToolConfigure.setIcon(IconHelper.fromResource(getClass(), "toolBar/configure.png"));
        centralwidget = new QWidget(MainWindow);
        centralwidget.setObjectName("centralwidget");
        horizontalLayout = new QHBoxLayout(centralwidget);
        horizontalLayout.setMargin(0);
        horizontalLayout.setObjectName("horizontalLayout");
        treeView = new QTreeView(centralwidget);
        treeView.setObjectName("treeView");

        horizontalLayout.addWidget(treeView);

        MainWindow.setCentralWidget(centralwidget);
        actionsSideBar = new QDockWidget(MainWindow);
        actionsSideBar.setObjectName("actionsSideBar");
        actionsSideBar.setMinimumSize(new QSize(200, 36));
        actionsSideBar.setFeatures(QDockWidget.DockWidgetFeature.createQFlags(QDockWidget.DockWidgetFeature.DockWidgetFloatable, QDockWidget.DockWidgetFeature.DockWidgetMovable));
        actionsSideBar.setAllowedAreas(Qt.DockWidgetArea.createQFlags(Qt.DockWidgetArea.LeftDockWidgetArea, Qt.DockWidgetArea.RightDockWidgetArea));
        actionsSideBarContent = new QWidget();
        actionsSideBarContent.setObjectName("actionsSideBarContent");
        actionsSideBar.setWidget(actionsSideBarContent);
        MainWindow.addDockWidget(com.trolltech.qt.core.Qt.DockWidgetArea.resolve(1), actionsSideBar);
        menuBar = new QMenuBar(MainWindow);
        menuBar.setObjectName("menuBar");
        menuBar.setGeometry(new QRect(0, 0, 800, 24));
        menuFile = new QMenu(menuBar);
        menuFile.setObjectName("menuFile");
        menuDisplay = new QMenu(menuBar);
        menuDisplay.setObjectName("menuDisplay");
        menuTools = new QMenu(menuBar);
        menuTools.setObjectName("menuTools");
        menuSettings = new QMenu(menuBar);
        menuSettings.setObjectName("menuSettings");
        menuHelp = new QMenu(menuBar);
        menuHelp.setObjectName("menuHelp");
        menuContact = new QMenu(menuBar);
        menuContact.setObjectName("menuContact");
        MainWindow.setMenuBar(menuBar);
        toolBar = new QToolBar(MainWindow);
        toolBar.setObjectName("toolBar");
        toolBar.setIconSize(new QSize(32, 32));
        toolBar.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextUnderIcon);
        MainWindow.addToolBar(Qt.ToolBarArea.TopToolBarArea, toolBar);

        menuBar.addAction(menuFile.menuAction());
        menuBar.addAction(menuDisplay.menuAction());
        menuBar.addAction(menuTools.menuAction());
        menuBar.addAction(menuSettings.menuAction());
        menuBar.addAction(menuHelp.menuAction());
        menuBar.addAction(menuContact.menuAction());
        menuFile.addAction(actionRun);
        menuFile.addAction(actionInstall);
        menuFile.addAction(actionRemove);
        menuFile.addSeparator();
        menuFile.addAction(actionDonate);
        menuFile.addAction(actionExit);
        menuDisplay.addAction(actionSmall_Icons);
        menuDisplay.addAction(actionMedium_Icons);
        menuDisplay.addAction(actionLarge_Icons);
        menuDisplay.addAction(actionVery_Large_Icons);
        menuTools.addAction(actionWineVersions);
        menuTools.addSeparator();
        menuTools.addAction(actionLocalScript);
        menuTools.addAction(actionConsole);
        menuTools.addAction(actionCloseAll);
        menuTools.addSeparator();
        menuTools.addAction(actionDebugger);
        menuSettings.addAction(actionNetwork);
        menuHelp.addAction(actionAbout);
        menuHelp.addSeparator();
        menuHelp.addAction(actionSoftware);
        menuHelp.addAction(actionNews);
        menuHelp.addAction(actionForums);
        menuHelp.addAction(actionBugs);
        menuContact.addAction(actionTwitter);
        menuContact.addAction(actionGooglePlus);
        menuContact.addAction(actionFacebook);
        toolBar.addAction(actionToolRun);
        toolBar.addAction(actionToolClose);
        toolBar.addSeparator();
        toolBar.addAction(actionToolInstall);
        toolBar.addAction(actionToolRemove);
        toolBar.addSeparator();
        toolBar.addAction(actionToolConfigure);
        retranslateUi(MainWindow);
        actionExit.changed.connect(MainWindow, "close()");

        MainWindow.connectSlotsByName();
    } // setupUi

    void retranslateUi(QMainWindow MainWindow) {
        MainWindow.setWindowTitle(translate("MainWindow"));
        actionRun.setText(translate("Run"));
        actionInstall.setText(translate("Install"));
        actionRemove.setText(translate("Remove"));
        actionDonate.setText(translate("Donate"));
        actionExit.setText(translate("Exit"));
        actionSmall_Icons.setText(translate("Small Icons"));
        actionMedium_Icons.setText(translate("Medium Icons"));
        actionLarge_Icons.setText(translate("Large Icons"));
        actionVery_Large_Icons.setText(translate("Very Large Icons"));
        actionWineVersions.setText(translate("Manage Wine versions"));
        actionLocalScript.setText(translate("Run a local script"));
        actionConsole.setText(translate("PlayOnLinux console"));
        actionCloseAll.setText(translate("Close all PlayOnLinux software"));
        actionDebugger.setText(translate("PlayOnLinux debugger"));
        actionNetwork.setText(translate("Network"));
        actionAbout.setText(translate("About"));
        actionSoftware.setText(translate("Supported Software"));
        actionNews.setText(translate("News"));
        actionForums.setText(translate("Forums"));
        actionBugs.setText(translate("Bugs"));
        actionGooglePlus.setText(translate("Google+"));
        actionTwitter.setText(translate("Twitter"));
        actionFacebook.setText(translate("Facebook"));
        actionToolRun.setText(translate("Run"));
        actionToolRun.setToolTip(translate("Run the currently selected shortcut"));
        actionToolClose.setText(translate("Close"));
        actionToolClose.setToolTip(translate("Close all instances of the currently selected shortcut"));
        actionToolInstall.setText(translate("Install"));
        actionToolInstall.setToolTip(translate("Install new software"));
        actionToolRemove.setText(translate("Remove"));
        actionToolRemove.setToolTip(translate("Delete the application that the currently selected shortcut belongs to"));
        actionToolConfigure.setText(translate("Configure"));
        actionToolConfigure.setToolTip(translate("Configure the currently selected shortcut"));
        actionsSideBar.setWindowTitle(translate("Actions"));
        menuFile.setTitle(translate("File"));
        menuDisplay.setTitle(translate("Display"));
        menuTools.setTitle(translate("Tools"));
        menuSettings.setTitle(translate("Settings"));
        menuHelp.setTitle(translate("Help"));
        menuContact.setTitle(translate("Contact"));
        toolBar.setWindowTitle(translate("toolBar"));
    } // retranslateUi

}

