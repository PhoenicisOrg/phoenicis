/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.filesystem;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.BiPredicate;

public class DirectoryWatcherFiles extends DirectoryWatcher<List<File>> {
    public DirectoryWatcherFiles(ExecutorService executorService, File observedDirectory) {
        super(executorService, observedDirectory);
    }

    public DirectoryWatcherFiles(ExecutorService executorService, File observedDirectory, int checkInterval) {
        super(executorService, observedDirectory, checkInterval);
    }

    @Override
    protected List<File> defineWatchedObject() {
        File[] files = observedDirectory.listFiles();
        List<File> filesFiltered = new LinkedList<>();
        assert files != null;
        for(File file: files) {
            if (!file.getName().startsWith(".")) {
                filesFiltered.add(file);
            }
        }

        return filesFiltered;
    }

    @Override
    protected BiPredicate<List<File>, List<File>> defineComparisonFunction() {
        return List::equals;
    }

}
