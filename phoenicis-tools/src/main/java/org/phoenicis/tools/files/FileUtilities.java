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
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A collection of file utilities available to the scripts
 */
@Safe
public class FileUtilities extends FilesManipulator {
    @Value("${application.user.tmp}")
    private String tmpDirectory;

    /**
     * Lists files and directories in a given directory (non-recursive)
     *
     * @param path The path representing the directory
     * @return list of files and directories
     */
    public String[] ls(String path) {
        final File directory = new File(path);

        if (!directory.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", path));
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" is not a directory", path));
        }

        assertInDirectory(directory);

        File[] files = directory.listFiles();

        if (files == null) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not denote a directory", path));
        }

        return Arrays.stream(files)
                .map(File::getName)
                .toArray(String[]::new);
    }

    /**
     * Checks whether a file with the given path exists
     *
     * @param path The path leading to the file
     * @return True if a file with the given path exists, false otherwise
     */
    public boolean exists(String path) {
        final File file = new File(path);

        assertInDirectory(file);

        return file.exists();
    }

    /**
     * Creates a directory with a given path
     *
     * @param path The path representing the directory
     */
    public void mkdir(String path) {
        final File directoryToCreate = new File(path);

        assertInDirectory(directoryToCreate);

        directoryToCreate.mkdirs();
    }

    /**
     * Creates a symbolic link from a target file to a destination file
     *
     * @param linkPath The path of the symbolic link to create
     * @param targetPath The target of the symbolic link
     * @throws IOException if an error during link creation occurs
     */
    public void createSymbolicLink(String linkPath, String targetPath) throws IOException {
        final File link = new File(linkPath);
        final File target = new File(targetPath);

        if (!target.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", targetPath));
        }

        if (link.exists()) {
            throw new IllegalArgumentException(String.format("Link \"%s\" already exists", linkPath));
        }

        assertInDirectory(link);
        assertInDirectory(target);

        Files.createSymbolicLink(link.toPath(), target.toPath());
    }

    /**
     * Copies all files contained in the given source path to the given target path
     *
     * @param sourcePath The source path
     * @param targetPath The target path
     * @throws IOException if an error during the copy operation occurs
     */
    public void copy(String sourcePath, String targetPath) throws IOException {
        final File source = new File(sourcePath);
        final File target = new File(targetPath);

        if (!source.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", sourcePath));
        }

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
     * Deletes a file denoted by a given path if it is inside the Phoenicis root location
     *
     * @param path The path denoting the to be deleted file
     */
    public void remove(String path) throws IOException {
        final File fileToDelete = new File(path);

        if (!fileToDelete.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", path));
        }

        assertInDirectory(fileToDelete);

        if (fileToDelete.isDirectory()) {
            FileUtils.deleteDirectory(fileToDelete);
        } else {
            Files.deleteIfExists(fileToDelete.toPath());
        }
    }

    /**
     * Fetches the file name of the file denoted by the given path
     *
     * @param path The path
     * @return The file name
     */
    public String getFileName(String path) {
        final File file = new File(path);

        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", path));
        }

        assertInDirectory(file);

        return file.getName();
    }

    /**
     * Fetches the content of the file located in the given path
     *
     * @param path The path to the file
     * @return The file content as an UTF-8 String
     * @throws IOException if an error while loading the file content occurs
     */
    public String getFileContent(String path) throws IOException {
        final File file = new File(path);

        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", path));
        }

        assertInDirectory(file);

        return FileUtils.readFileToString(file, "UTF-8");
    }

    /**
     * Computes the file size of the file or directory denoted by the given path
     *
     * @param path The path
     * @return The size of the given file/directory
     * @throws IOException if an error while retrieving the file/folder size occurs
     */
    public long getSize(String path) throws IOException {
        final File file = new File(path);

        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", path));
        }

        assertInDirectory(file);

        Path folder = Paths.get(file.getAbsolutePath());

        return Files.walk(folder)
                .filter(p -> p.toFile().isFile())
                .mapToLong(p -> p.toFile().length())
                .sum();
    }

    /**
     * Writes the given content String to the file denoted by the given path.
     * If the file already exists its content is replaced with the given String, otherwise the file is created
     *
     * @param path The path leading to the destination file
     * @param content The file content encoded as an UTF-8 String
     * @throws IOException if an error during the write operation occurs
     */
    public void writeToFile(String path, String content) throws IOException {
        final File file = new File(path);

        assertInDirectory(file);

        FileUtils.writeStringToFile(file, content, "UTF-8");
    }

    /**
     * Creates a temporary file with the given file extension
     *
     * @param extension The file extension
     * @return The path leading to the created temporary file
     * @throws IOException if an error during the temporary file creation occurs
     */
    public String createTmpFile(String extension) throws IOException {
        final File tmpDirectoryFile = new File(tmpDirectory);

        tmpDirectoryFile.mkdirs();
        final File file = File.createTempFile("phoenicis", "." + extension, tmpDirectoryFile);
        file.deleteOnExit();

        return file.getAbsolutePath();
    }

    /**
     * Creates a temporary directory
     *
     * @return The path of the created temporary directory
     * @throws IOException if an error during the temporary directory creation occurs
     */
    public String createTmpDir() throws IOException {
        final File tmpDirectoryFile = new File(tmpDirectory);

        tmpDirectoryFile.mkdirs();
        final File file = Files.createTempDirectory(tmpDirectoryFile.toPath(), "phoenicis").toFile();
        file.deleteOnExit();

        return file.getAbsolutePath();
    }

    public void chmod(String filePath, String permissions) throws IOException {
        final Path path = Paths.get(filePath);

        if (!path.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Path \"%s\" does not exist", path));
        }

        final Set<PosixFilePermission> permissionsObj = PosixFilePermissions.fromString(permissions);

        Files.setPosixFilePermissions(path, permissionsObj);
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
