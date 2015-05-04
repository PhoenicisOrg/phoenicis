package com.playonlinux.ui.api;

import java.util.Iterator;
import java.util.Observer;

public interface InstalledApplications {
    void addObserver(Observer o);

    Iterator<Shortcut> getShortcuts();
}
