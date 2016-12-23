package com.playonlinux.tools.files;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;

import static java.lang.String.format;

class FilesManipulator {
    @Value("${application.user.root}")
    private String userRoot;

    private boolean isInSubDirectory(File directory, File fileIside) {
        return fileIside != null && (fileIside.equals(directory) || isInSubDirectory(directory, fileIside.getParentFile()));
    }

    void assertInDirectory(File file) {
        if(!isInSubDirectory(new File(userRoot), file)) {
            throw new IllegalArgumentException(format("The file (%s) must be in a the PlayOnLinux root directory (%s)",
                    file, userRoot));
        }
    }

}
