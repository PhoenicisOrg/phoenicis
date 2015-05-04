package com.playonlinux.ui.api;

import com.playonlinux.ui.beans.ShortcutBean;

import java.util.Observer;

public interface InstalledApplications extends Iterable<ShortcutBean> {
    void addObserver(Observer o);
}
