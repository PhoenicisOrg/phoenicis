package com.playonlinux.tools.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileSearcher extends FilesManipulator {
    public List<File> search(String directory, String name) {
        return search(new File(directory), name);
    }

    public List<File> search(File directory, String name) {
        assertInDirectory(directory);
        if(!directory.exists()) {
            return Collections.emptyList();
        }
        final List<File> results = new ArrayList<>();

        Path startPath = Paths.get(directory.getAbsolutePath());
        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (name.equals(file.getFileName().toString())) {
                        results.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return results;
    }
}
