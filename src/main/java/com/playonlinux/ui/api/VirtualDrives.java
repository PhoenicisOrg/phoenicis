package com.playonlinux.ui.api;

import com.playonlinux.ui.dtos.VirtualDriveDTO;

import java.util.Observer;

public interface VirtualDrives extends Iterable<VirtualDriveDTO> {
    void addObserver(Observer o);
}
