package com.playonlinux.ui.impl.gtk.mainwindow;


import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.events.EventHandler;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import org.gnome.gtk.FileChooserAction;
import org.gnome.gtk.FileChooserDialog;
import org.gnome.gtk.Window;

import java.io.File;

@Scan
public class MainWindowEventDispatcher {
    @Inject
    static EventHandler mainEventHandler;

    private final Window parent;

    public MainWindowEventDispatcher(Window parent) {
        this.parent = parent;
    }

    public void runLocalScript() throws PlayOnLinuxException {
        FileChooserDialog fileChooserDialog =
                new FileChooserDialog("Select a script to run", parent, FileChooserAction.OPEN);

        try {
            fileChooserDialog.run();
        } catch (org.gnome.glib.FatalError ignored) {
            // FIXME: Catch properly this exception
        }

        if (fileChooserDialog.getFilename() != null) {
            File scriptPath = new File(fileChooserDialog.getFilename());
            fileChooserDialog.hide();

            mainEventHandler.runLocalScript(scriptPath);
        } else {
            fileChooserDialog.hide();
        }
    }
}
