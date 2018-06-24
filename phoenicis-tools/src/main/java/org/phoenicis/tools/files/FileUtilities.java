/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.tools.files;

import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Safe
public class FileUtilities extends FilesManipulator {
    @Value("${application.user.tmp}")
    private String tmpDirectory;

    /**
     * lists files and directories in given directory (non-recursive)
     * @param directory
     * @return list of files and directories
     */
    public String[] ls(File directory) {
        assertInDirectory(directory);
        File[] files = directory.listFiles();
        return Arrays.stream(files)
                .map(file -> file.getName())
                .toArray(String[]::new);
    }

    /**
     * creates a directory
     * @param directoryToCreate
     */
    public void mkdir(File directoryToCreate) {
        assertInDirectory(directoryToCreate);
        directoryToCreate.mkdirs();
    }

    /**
     * creates symbolic link
     * @param destination
     * @param target
     * @throws IOException
     */
    public void createSymbolicLink(File destination, File target) throws IOException {
        assertInDirectory(destination);
        assertInDirectory(target);

        Files.createSymbolicLink(destination.toPath(), target.toPath());
    }

    /**
     * copies file
     * @param source
     * @param target
     * @throws IOException
     */
    public void copy(File source, File target) throws IOException {
        assertInDirectory(source);
        assertInDirectory(target);

        if (source.isDirectory()) {
            FileUtils.copyDirectory(source, target);
        } else {
            if (target.isDirectory()) {
                FileUtils.copyFile(source, new File(target, source.getName()));
            } else {
                FileUtils.copyFile(source, target);
            }
        }
    }

    /**
     * deletes a file only if it is inside Phoenicis root
     * @param fileToDelete fileOrDirectoryToDelete
     */
    public void remove(File fileToDelete) throws IOException {
        assertInDirectory(fileToDelete);

        if (fileToDelete.isDirectory()) {
            FileUtils.deleteDirectory(fileToDelete);
        } else {
            Files.deleteIfExists(fileToDelete.toPath());
        }
    }

    /**
     * fetches content of the given file
     * @param file
     * @return content string
     * @throws IOException
     */
    public String getFileContent(File file) throws IOException {
        assertInDirectory(file);

        return FileUtils.readFileToString(file, "UTF-8");
    }

    /**
     * computes file size of the given file
     * @param file
     * @return file size
     * @throws IOException
     */
    public long getSize(File file) throws IOException {
        assertInDirectory(file);

        Path folder = Paths.get(file.getAbsolutePath());
        return Files.walk(folder).filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum();
    }

    /**
     * writes content to file
     * @param file
     * @param content
     * @throws IOException
     */
    public void writeToFile(File file, String content) throws IOException {
        assertInDirectory(file);

        FileUtils.writeStringToFile(file, content, "UTF-8");
    }

    /**
     * creates temporary file
     * @param extension
     * @return
     * @throws IOException
     */
    public File createTmpFile(String extension) throws IOException {
        final File tmpDirectoryFile = new File(tmpDirectory);
        tmpDirectoryFile.mkdirs();
        final File file = File.createTempFile("phoenicis", "." + extension, tmpDirectoryFile);
        file.deleteOnExit();
        return file;
    }

    private Set<PosixFilePermission> singleIntToFilePermission(Integer mode, String groupType) {
        Set<PosixFilePermission> permissions = new HashSet<>(9);

        if (Arrays.asList(new Integer[] { 1, 3, 5, 7 }).contains(mode)) {
            permissions.add(PosixFilePermission.valueOf(groupType + "_EXECUTE"));
        }

        if (Arrays.asList(new Integer[] { 2, 3, 6, 7 }).contains(mode)) {
            permissions.add(PosixFilePermission.valueOf(groupType + "_WRITE"));
        }

        if (Arrays.asList(new Integer[] { 4, 5, 6, 7 }).contains(mode)) {
            permissions.add(PosixFilePermission.valueOf(groupType + "_READ"));
        }

        return permissions;
    }

    public Set<PosixFilePermission> intToPosixFilePermission(int mode) {
        if (mode >= 1000 || mode < 0) {
            throw new IllegalArgumentException("Invalid mode " + mode);
        }

        final int owner = mode / 100;
        final int group = (mode - owner * 100) / 10;
        final int others = mode - owner * 100 - group * 10;

        if (owner > 7 || group > 7 || others > 7) {
            throw new IllegalArgumentException("Invalid mode " + mode);
        }

        return Sets.union(
                Sets.union(singleIntToFilePermission(owner, "OWNER"), singleIntToFilePermission(group, "GROUP")),
                singleIntToFilePermission(others, "OTHERS"));
    }

    public Set<PosixFilePermission> octToPosixFilePermission(int modeOct) {
        // TODO: optimize this method and make it cleaner
        int modeInt = Integer.parseInt(Integer.toString(modeOct, 8));

        return intToPosixFilePermission(modeInt);
    }
}
