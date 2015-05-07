package com.playonlinux.domain;

import java.io.File;

public class VirtualDrive {
    private final File virtualDriveFile;

    public VirtualDrive(File virtualDriveFile) {
        this.virtualDriveFile = virtualDriveFile;
    }

    public String getName() {
        return virtualDriveFile.getName();
    }
}
