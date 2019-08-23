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

package org.phoenicis.tools.archive;

import com.google.common.io.CountingInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Safe
public class Zip {
    private static final String ZIP_ERROR_MESSAGE = "Unable to open input stream";
    private final Logger LOGGER = LoggerFactory.getLogger(Zip.class);

    public List<File> uncompressZipFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        final long finalSize = FileUtils.sizeOf(inputFile);
        final AtomicLong counter = new AtomicLong(0);

        LOGGER.info(String.format("Attempting to unzip file \"%s\" to directory \"%s\".",
                inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

        Path targetDirPath = outputDir.toPath();
        try (ZipFile zipFile = new ZipFile(inputFile)) {
            return Collections.list(zipFile.getEntries()).parallelStream()
                    .map(entry -> unzipEntry(zipFile, entry, targetDirPath, counter, finalSize, stateCallback))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ArchiveException(ZIP_ERROR_MESSAGE, e);
        }
    }

    private File unzipEntry(ZipFile zipFile, ZipArchiveEntry entry, Path targetDir, AtomicLong counter,
            double finalSize, Consumer<ProgressEntity> stateCallback) {
        try {
            Path targetPath = targetDir.resolve(Paths.get(entry.getName()));

            if (entry.isDirectory()) {
                LOGGER.info(String.format("Attempting to create output directory %s.", targetPath.toString()));

                Files.createDirectories(targetPath);
            } else {
                Files.createDirectories(targetPath.getParent());

                try (CountingInputStream in = new CountingInputStream(zipFile.getInputStream(entry))) {
                    LOGGER.info(String.format("Creating output file %s.", targetPath.toString()));

                    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

                    double count = counter.addAndGet(in.getCount());

                    stateCallback
                            .accept(new ProgressEntity.Builder()
                                    .withPercent(count / finalSize * 100.0D)
                                    .withProgressText("Extracting " + entry.getName()).build());
                }
            }

            return targetPath.toFile();
        } catch (IOException e) {
            throw new ArchiveException(String.format("Unable to extract file \"%s\"", entry.getName()), e);
        }
    }
}
