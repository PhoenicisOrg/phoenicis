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

package com.playonlinux.ui.impl.qt.mainwindow.menubar;

import static com.playonlinux.core.lang.Localisation.translate;

import com.playonlinux.ui.impl.qt.mainwindow.MainWindow;
import com.playonlinux.ui.impl.qt.mainwindow.shortcuts.ShortcutList;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QActionGroup;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMenuBar;

/**
 * MenuBar of the MainWindow
 */
public class MenuBar extends QMenuBar {
    private final MainWindow mainWindow;

    private QMenu menuFile;
    private QMenu menuDisplay;
    private QMenu menuTools;
    private QMenu menuSettings;
    private QMenu menuHelp;
    private QMenu menuContact;

    private QAction actionRun;
    private QAction actionInstall;
    private QAction actionRemove;
    private QAction actionDonate;
    private QAction actionExit;
    private QAction actionSmall_Icons;
    private QAction actionMedium_Icons;
    private QAction actionLarge_Icons;
    private QAction actionVery_Large_Icons;
    private QAction actionWineVersions;
    private QAction actionLocalScript;
    private QAction actionConsole;
    private QAction actionCloseAll;
    private QAction actionDebugger;
    private QAction actionNetwork;
    private QAction actionAbout;
    private QAction actionSoftware;
    private QAction actionNews;
    private QAction actionForums;
    private QAction actionBugs;
    private QAction actionGooglePlus;
    private QAction actionTwitter;
    private QAction actionFacebook;

    private QActionGroup displayIconSizeGroup;

    public MenuBar(MainWindow mainWindow) {
        super(mainWindow);
        this.mainWindow = mainWindow;

        setupUi();
        retranslateUi();
        connectSlots();
    }

    private void setupUi() {
        /* MENU: FILE */
        menuFile = new QMenu(this);

        actionRun = new QAction(mainWindow);
        actionRun.setIcon(QIcon.fromTheme("document-open"));
        actionInstall = new QAction(mainWindow);
        actionInstall.setIcon(QIcon.fromTheme("list-add"));
        actionRemove = new QAction(mainWindow);
        actionRemove.setIcon(QIcon.fromTheme("edit-delete"));
        actionDonate = new QAction(mainWindow);
        actionDonate.setIcon(QIcon.fromTheme("help-donate"));
        actionExit = new QAction(mainWindow);
        actionExit.setIcon(QIcon.fromTheme("application-exit"));

        menuFile.addAction(actionRun);
        menuFile.addAction(actionInstall);
        menuFile.addAction(actionRemove);
        menuFile.addSeparator();
        menuFile.addAction(actionDonate);
        menuFile.addAction(actionExit);

        /* MENU: DISPLAY */
        menuDisplay = new QMenu(this);

        actionSmall_Icons = new QAction(mainWindow);
        actionSmall_Icons.setCheckable(true);
        actionMedium_Icons = new QAction(mainWindow);
        actionMedium_Icons.setCheckable(true);
        actionLarge_Icons = new QAction(mainWindow);
        actionLarge_Icons.setCheckable(true);
        actionVery_Large_Icons = new QAction(mainWindow);
        actionVery_Large_Icons.setCheckable(true);

        menuDisplay.addAction(actionSmall_Icons);
        menuDisplay.addAction(actionMedium_Icons);
        menuDisplay.addAction(actionLarge_Icons);
        menuDisplay.addAction(actionVery_Large_Icons);

        displayIconSizeGroup = new QActionGroup(menuDisplay);
        displayIconSizeGroup.addAction(actionSmall_Icons);
        displayIconSizeGroup.addAction(actionMedium_Icons);
        displayIconSizeGroup.addAction(actionLarge_Icons);
        displayIconSizeGroup.addAction(actionVery_Large_Icons);

        /* MENU: TOOLS */
        menuTools = new QMenu(this);

        actionWineVersions = new QAction(mainWindow);
        actionWineVersions.setIcon(QIcon.fromTheme("wine"));
        actionLocalScript = new QAction(mainWindow);
        actionLocalScript.setIcon(QIcon.fromTheme("application-x-shellscript"));
        actionConsole = new QAction(mainWindow);
        actionConsole.setIcon(QIcon.fromTheme("utilities-terminal"));

        actionCloseAll = new QAction(mainWindow);
        actionCloseAll.setIcon(QIcon.fromTheme("process-stop"));
        actionDebugger = new QAction(mainWindow);
        actionDebugger.setIcon(QIcon.fromTheme("debug-run"));

        menuTools.addAction(actionWineVersions);
        menuTools.addSeparator();
        menuTools.addAction(actionLocalScript);
        menuTools.addAction(actionConsole);
        menuTools.addAction(actionCloseAll);
        menuTools.addSeparator();
        menuTools.addAction(actionDebugger);

        /* MENU: SETTINGS */
        menuSettings = new QMenu(this);

        actionNetwork = new QAction(mainWindow);
        actionNetwork.setIcon(QIcon.fromTheme("network-wired"));

        menuSettings.addAction(actionNetwork);

        /* MENU: HELP */
        menuHelp = new QMenu(this);

        actionAbout = new QAction(mainWindow);
        actionAbout.setIcon(QIcon.fromTheme("help-about"));
        actionSoftware = new QAction(mainWindow);
        actionSoftware.setIcon(QIcon.fromTheme("applications-other"));
        actionNews = new QAction(mainWindow);
        actionNews.setIcon(QIcon.fromTheme("message-news"));
        actionForums = new QAction(mainWindow);
        actionForums.setIcon(QIcon.fromTheme("user-identity"));
        actionBugs = new QAction(mainWindow);
        actionBugs.setIcon(QIcon.fromTheme("tools-report-bug"));

        menuHelp.addAction(actionAbout);
        menuHelp.addSeparator();
        menuHelp.addAction(actionSoftware);
        menuHelp.addAction(actionNews);
        menuHelp.addAction(actionForums);
        menuHelp.addAction(actionBugs);

        /* MENU: CONTACT */
        menuContact = new QMenu(this);

        actionGooglePlus = new QAction(mainWindow);
        actionTwitter = new QAction(mainWindow);
        actionFacebook = new QAction(mainWindow);

        menuContact.addAction(actionTwitter);
        menuContact.addAction(actionGooglePlus);
        menuContact.addAction(actionFacebook);

        addAction(menuFile.menuAction());
        addAction(menuDisplay.menuAction());
        addAction(menuTools.menuAction());
        addAction(menuSettings.menuAction());
        addAction(menuHelp.menuAction());
        addAction(menuContact.menuAction());
    }

    private void retranslateUi() {
        /* MENU: FILE */
        menuFile.setTitle(translate("File"));
        actionRun.setText(translate("Run"));
        actionInstall.setText(translate("Install"));
        actionRemove.setText(translate("Remove"));
        actionDonate.setText(translate("Donate"));
        actionExit.setText(translate("Exit"));

        /* MENU: DISPLAY */
        menuDisplay.setTitle(translate("Display"));
        actionSmall_Icons.setText(translate("Small Icons"));
        actionMedium_Icons.setText(translate("Medium Icons"));
        actionLarge_Icons.setText(translate("Large Icons"));
        actionVery_Large_Icons.setText(translate("Very Large Icons"));

        /* MENU: TOOLS */
        menuTools.setTitle(translate("Tools"));
        actionWineVersions.setText(translate("Manage Wine versions"));
        actionLocalScript.setText(translate("Run a local script"));
        actionConsole.setText(translate("PlayOnLinux console"));
        actionCloseAll.setText(translate("Close all PlayOnLinux software"));
        actionDebugger.setText(translate("PlayOnLinux debugger"));

        /* MENU: SETTINGS */
        menuSettings.setTitle(translate("Settings"));
        actionNetwork.setText(translate("Network"));

        /* MENU: HELP */
        menuHelp.setTitle(translate("Help"));
        actionAbout.setText(translate("About"));
        actionSoftware.setText(translate("Supported Software"));
        actionNews.setText(translate("News"));
        actionForums.setText(translate("Forums"));
        actionBugs.setText(translate("Bugs"));

        /* MENU: CONTACT */
        menuContact.setTitle(translate("Contact"));
        actionGooglePlus.setText(translate("Google+"));
        actionTwitter.setText(translate("Twitter"));
        actionFacebook.setText(translate("Facebook"));
    }

    private void connectSlots() {
        /* MENU: FILES */
        actionRun.triggered.connect(this, "actionRunTriggered()");
        actionInstall.triggered.connect(this, "actionInstallTriggered()");
        actionRemove.triggered.connect(this, "actionRemoveTriggered()");
        actionDonate.triggered.connect(this, "actionDonate_triggered()");
        actionExit.triggered.connect(this, "actionExitTriggered()");

        /* MENU: DISPLAY */
        actionSmall_Icons.triggered.connect(this, "actionSmallIconsTriggered(boolean)");
        actionMedium_Icons.triggered.connect(this, "actionMediumIconsTriggered(boolean)");
        actionLarge_Icons.triggered.connect(this, "actionLargeIconsTriggered(boolean)");
        actionVery_Large_Icons.triggered.connect(this, "actionVeryLargeIconsTriggered(boolean)");

        /* MENU: TOOLS */
        actionWineVersions.triggered.connect(this, "actionWineVersionsTriggered()");
        actionLocalScript.triggered.connect(this, "actionLocalScript_triggered()");
        actionConsole.triggered.connect(this, "actionConsoleTriggered()");
        actionCloseAll.triggered.connect(this, "actionCloseAllTriggered()");
        actionDebugger.triggered.connect(this, "actionDebuggerTriggered()");

        /* MENU: SETTINGS */
        actionNetwork.triggered.connect(this, "actionNetworkTriggered()");

        /* MENU: HELP */
        actionAbout.triggered.connect(this, "actionAboutTriggered()");
        actionSoftware.triggered.connect(this, "actionSoftwareTriggered()");
        actionNews.triggered.connect(this, "actionNewsTriggered()");
        actionForums.triggered.connect(this, "actionForumsTriggered()");
        actionBugs.triggered.connect(this, "actionBugsTriggered()");

        /* MENU: CONTACT */
        actionGooglePlus.triggered.connect(this, "actionGooglePlusTriggered()");
        actionTwitter.triggered.connect(this, "actionTwitterTriggered()");
        actionFacebook.triggered.connect(this, "actionFacebookTriggered()");
    }

    /* SIGNAL HANDLERS */

    // FILE
    private void actionRunTriggered() {
        mainWindow.getEventHandler().runLocalScript();
    }

    private void actionInstallTriggered() {
    }

    private void actionRemoveTriggered() {
    }

    private void actionDonate_triggered() {
    }

    private void actionExitTriggered() {
        mainWindow.getEventHandler().exit();
    }

    // DISPLAY
    private void actionSmallIconsTriggered(boolean checked) {
        if (checked) {
            mainWindow.getEventHandler().setDisplaySize(ShortcutList.IconSize.SMALL);
        }
    }

    private void actionMediumIconsTriggered(boolean checked) {
        if (checked) {
            mainWindow.getEventHandler().setDisplaySize(ShortcutList.IconSize.MEDIUM);
        }
    }

    private void actionLargeIconsTriggered(boolean checked) {
        if (checked) {
            mainWindow.getEventHandler().setDisplaySize(ShortcutList.IconSize.LARGE);
        }
    }

    private void actionVeryLargeIconsTriggered(boolean checked) {
        if (checked) {
            mainWindow.getEventHandler().setDisplaySize(ShortcutList.IconSize.VERY_LARGE);
        }
    }

    // TOOLS
    private void actionWineVersionsTriggered() {
    }

    private void actionLocalScript_triggered() {
    }

    private void actionConsoleTriggered() {
    }

    private void actionCloseAllTriggered() {
    }

    private void actionDebuggerTriggered() {
    }

    // SETTINGS
    private void actionNetworkTriggered() {
    }

    // HELP
    private void actionAboutTriggered() {
    }

    private void actionSoftwareTriggered() {
        mainWindow.getEventHandler().openLink("https://www.playonlinux.com/en/supported_apps.html");
    }

    private void actionNewsTriggered() {
        mainWindow.getEventHandler().openLink("https://www.playonlinux.com/en/news.html");
    }

    private void actionForumsTriggered() {
        mainWindow.getEventHandler().openLink("https://www.playonlinux.com/en/forums.html");
    }

    private void actionBugsTriggered() {
        mainWindow.getEventHandler().openLink("https://www.playonlinux.com/en/bugs.html");
    }

    // CONTACT
    private void actionGooglePlusTriggered() {
        mainWindow.getEventHandler().openLink("https://plus.google.com/+playonlinux");
    }

    private void actionTwitterTriggered() {
        mainWindow.getEventHandler().openLink("https://twitter.com/PlayOnLinux");
    }

    private void actionFacebookTriggered() {
        mainWindow.getEventHandler().openLink("https://www.facebook.com/playonlinux");
    }

}
