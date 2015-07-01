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

package com.playonlinux.ui.impl.gtk;

import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.services.EventDispatcher;
import org.gnome.gtk.*;

import java.io.File;
import java.io.IOException;

@Scan
public class GTKApplication extends Window {

    @Inject
    static EventDispatcher mainEventDispatcher;

    public GTKApplication() throws IOException {
        setTitle("PlayOnLinux");

        connect((DeleteEvent) (source, event) -> {
            Gtk.mainQuit();
            return false;
        });

        setDefaultSize(250, 150);
        setPosition(WindowPosition.CENTER);
        show();

        FileChooserDialog fileChooserDialog =
                new FileChooserDialog("Select a script to run", this, FileChooserAction.OPEN);

        try {
            fileChooserDialog.run();
        } catch(org.gnome.glib.FatalError ignored) {
            // FIXME: Catch properly this exception
        }

        if(fileChooserDialog.getFilename() != null) {
            File scriptPath = new File(fileChooserDialog.getFilename());
            fileChooserDialog.destroy();

            mainEventDispatcher.runLocalScript(scriptPath);
        }

    }
}
