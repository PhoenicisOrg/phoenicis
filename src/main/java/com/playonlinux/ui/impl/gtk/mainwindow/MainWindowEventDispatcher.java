package com.playonlinux.ui.impl.gtk.mainwindow;

import java.io.File;

import org.apache.log4j.Logger;
import org.gnome.gtk.FileChooserAction;
import org.gnome.gtk.FileChooserDialog;
import org.gnome.gtk.Window;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.ui.events.EventHandler;

@Scan
public class MainWindowEventDispatcher {
    private static final Logger LOGGER = Logger.getLogger(MainWindowEventDispatcher.class);

    @Inject
    static EventHandler mainEventHandler;

    private final Window parent;

    public MainWindowEventDispatcher(Window parent) {
        this.parent = parent;
    }

    public void runLocalScript() throws PlayOnLinuxException {
        FileChooserDialog fileChooserDialog = new FileChooserDialog("Select a script to run", parent,
                FileChooserAction.OPEN);

        try {
            fileChooserDialog.run();
        } catch (org.gnome.glib.FatalError e) {
            LOGGER.debug(e);
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
