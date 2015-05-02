package com.playonlinux.app;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class InstalledApplications extends Observable implements Observer {
    public InstalledApplications(File shortcutDirectory, File iconDirectory) {

    }

    @Override
    public void update(Observable o, Object arg) {
        this.notifyObservers();
    }
}
