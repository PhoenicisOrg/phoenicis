package com.playonlinux.ui.impl.javafx.configurewindow;

import com.playonlinux.ui.impl.configurewindow.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.JavaFXEventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ConfigureWindow extends Stage implements PlayOnLinuxWindow {
    private final Pane root;
    private final PlayOnLinuxWindow parent;
    private static ConfigureWindow instance;

    /**
     * Get the instance of the configure window.
     * The singleton pattern is only meant to avoid opening this window twice.
     * @param parent
     * @return the configureWindow instance
     */
    public static ConfigureWindow getInstance(PlayOnLinuxWindow parent) {
        if(instance == null) {
            instance = new ConfigureWindow(parent);
        } else {
            instance.toFront();
        }
        return instance;
    }

    private ConfigureWindow(PlayOnLinuxWindow parent) {
        super();
        this.parent = parent;
        this.root = new Pane();
        Scene scene = new Scene(root, 520, 400);

        this.setScene(scene);
        this.show();
    }

    @Override
    public JavaFXEventHandler getEventHandler() {
        return parent.getEventHandler();
    }
}

