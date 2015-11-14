package com.playonlinux.ui.impl.gtk.mainwindow;

import static com.playonlinux.core.lang.Localisation.translate;

import org.apache.log4j.Logger;
import org.gnome.gtk.Menu;
import org.gnome.gtk.MenuBar;
import org.gnome.gtk.MenuItem;
import org.gnome.gtk.SeparatorMenuItem;
import org.gnome.gtk.Widget;

import com.playonlinux.app.PlayOnLinuxException;

public class MainWindowMenuBar extends MenuBar {
    private static final Logger LOGGER = Logger.getLogger(MainWindowMenuBar.class);
    private final MainWindowEventDispatcher mainWindowEventDispatcher;

    public MainWindowMenuBar(GTKApplication parent) {
        this.mainWindowEventDispatcher = parent.getMainWindowEventDispatcher();
        this.append(createFileMenu());
        this.append(createToolsMenu());
    }

    private Widget createToolsMenu() {
        final MenuItem toolsItem = new MenuItem(translate("Tools"));

        final Menu toolsMenu = new Menu();
        final MenuItem runScriptItem = new MenuItem(translate("Run a local script"));

        runScriptItem.connect((MenuItem menuItem) -> {
            try {
                mainWindowEventDispatcher.runLocalScript();
            } catch (PlayOnLinuxException e) {
                LOGGER.warn(e);
            }
        });
        toolsMenu.append(runScriptItem);

        toolsItem.setSubmenu(toolsMenu);

        return toolsItem;
    }

    private MenuItem createFileMenu() {
        final MenuItem fileItem = new MenuItem(translate("File"));

        final Menu fileMenu = new Menu();
        final MenuItem runItem = new MenuItem(translate("Run"));
        final MenuItem installItem = new MenuItem(translate("Install"));
        final MenuItem removeItem = new MenuItem(translate("Remove"));
        final MenuItem quitItem = new MenuItem(translate("Quit"));

        fileMenu.append(runItem);
        fileMenu.append(installItem);
        fileMenu.append(removeItem);
        fileMenu.append(new SeparatorMenuItem());
        fileMenu.append(quitItem);
        fileItem.setSubmenu(fileMenu);

        return fileItem;
    }
}
