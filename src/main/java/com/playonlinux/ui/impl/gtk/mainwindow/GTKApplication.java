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

package com.playonlinux.ui.impl.gtk.mainwindow;

import org.gnome.gtk.Gtk;
import org.gnome.gtk.MenuBar;
import org.gnome.gtk.VBox;
import org.gnome.gtk.Window;
import org.gnome.gtk.WindowPosition;

public class GTKApplication extends Window {
    private MainWindowEventDispatcher mainWindowEventDispatcher;

    public GTKApplication() {
        setTitle("PlayOnLinux");

        mainWindowEventDispatcher = new MainWindowEventDispatcher(this);

        connect((DeleteEvent) (source, event) -> {
            Gtk.mainQuit();
            return false;
        });

        VBox vBox = new VBox(false, 0);

        MenuBar menuBar = new MainWindowMenuBar(this);

        vBox.packStart(menuBar, false, false, 3);
        setDefaultSize(515, 450);
        setPosition(WindowPosition.CENTER);

        add(vBox);

        showAll();

    }

    public MainWindowEventDispatcher getMainWindowEventDispatcher() {
        return mainWindowEventDispatcher;
    }
}
