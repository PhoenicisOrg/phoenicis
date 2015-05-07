package com.playonlinux.ui.impl.javafx.configurewindow;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.ui.impl.configurewindow.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.JavaFXEventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ConfigureWindow extends Stage implements PlayOnLinuxWindow {
    private final PlayOnLinuxWindow parent;
    private static ConfigureWindow instance;
    private final VirtualDrivesWidget installedVirtualDrivesWidget;

    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent
     * @return the configureWindow instance
     */
    public static ConfigureWindow getInstance(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        if(instance == null) {
            instance = new ConfigureWindow(parent);
        } else {
            instance.toFront();
        }
        return instance;
    }

    private ConfigureWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        super();
        this.parent = parent;

        this.installedVirtualDrivesWidget = new VirtualDrivesWidget();


        Scene scene = new Scene(installedVirtualDrivesWidget, 620, 400);


        this.setScene(scene);
        this.setUpEvents();
        this.show();
    }

    public void setUpEvents() throws PlayOnLinuxError {
        InstalledVirtualDrives installedVirtualDrives = getEventHandler().getInstalledVirtualDrives();
        installedVirtualDrives.addObserver(this.installedVirtualDrivesWidget);

    }
    @Override
    public JavaFXEventHandler getEventHandler() {
        return parent.getEventHandler();
    }
}

