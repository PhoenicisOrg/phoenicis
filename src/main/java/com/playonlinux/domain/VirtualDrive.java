package com.playonlinux.domain;

import java.io.File;

public class VirtualDrive {
    private final File virtualDriveFile;
    private File icon;

    public VirtualDrive(File virtualDriveFile) {
        this.virtualDriveFile = virtualDriveFile;
    }

    public String getName() {
        return virtualDriveFile.getName();
    }

    public File getIcon() {
        return icon;
    }
}
