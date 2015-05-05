package com.playonlinux.ui.api;

import com.playonlinux.ui.dtos.ShortcutDTO;

import java.util.Observer;

public interface InstalledApplications extends Iterable<ShortcutDTO> {
    void addObserver(Observer o);
}
