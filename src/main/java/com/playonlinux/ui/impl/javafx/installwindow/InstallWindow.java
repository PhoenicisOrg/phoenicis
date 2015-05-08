package com.playonlinux.ui.impl.javafx.installwindow;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.ui.impl.configurewindow.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.JavaFXEventHandler;
import com.playonlinux.ui.impl.javafx.configurewindow.VirtualDrivesWidget;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InstallWindow extends Stage implements PlayOnLinuxWindow {
    private final PlayOnLinuxWindow parent;
    private static InstallWindow instance;

    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent
     * @return the install window instance
     */
    public static InstallWindow getInstance(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        if(instance == null) {
            instance = new InstallWindow(parent);
        } else {
            instance.toFront();
        }
        return instance;
    }

    private InstallWindow(PlayOnLinuxWindow parent) throws PlayOnLinuxError {
        super();
        this.parent = parent;

        Scene scene = new Scene(new Pane(), 620, 400);

        this.setScene(scene);
        this.show();
    }


    @Override
    public JavaFXEventHandler getEventHandler() {
        return parent.getEventHandler();
    }
}

