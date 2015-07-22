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

package com.playonlinux.utils.archive;

import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.dto.ui.ProgressStateDTO;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.apache.log4j.Logger;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import java.util.function.Function;
import java.util.zip.GZIPInputStream;


/**
 * Tar extraction utilities
 */
public class Tar  {

    private static final Logger LOGGER = Logger.getLogger(Tar.class);

    List<File> uncompressTarBz2File(File inputFile, File outputDir, long finalSize, Function<ProgressStateDTO, Void> stateCallback) throws ArchiveException {
        try(InputStream inputStream = new BZip2CompressorInputStream(new FileInputStream(inputFile))) {
            return uncompress(inputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException("Unable to open input stream", e);
        }
    }

    List<File> uncompressTarGzFile(File inputFile, File outputDir, long finalSize, Function<ProgressStateDTO, Void> stateCallback) throws ArchiveException {
        try(InputStream inputStream = new GZIPInputStream(new FileInputStream(inputFile))) {
            return uncompress(inputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException("Unable to open input stream", e);
        }
    }

    List<File> uncompressTarFile(File inputFile, File outputDir, long finalSize, Function<ProgressStateDTO, Void> stateCallback) throws ArchiveException {
        try(InputStream inputStream = new FileInputStream(inputFile)) {
            return uncompress(inputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException("Unable to open input stream", e);
        }
    }

    /**
     * Uncompress a tar
     * @param outputDir The directory where files should be extracted
     * @return A list of extracted files
     * @throws ArchiveException if the process fails
     */
    private List<File> uncompress(final InputStream inputStream, final File outputDir, long finalSize,
                                  Function<ProgressStateDTO, Void> stateCallback) throws ArchiveException {
        final List<File> uncompressedFiles = new LinkedList<>();
        try(ArchiveInputStream debInputStream = new ArchiveStreamFactory().createArchiveInputStream("tar", inputStream)) {
            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
                final File outputFile = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    LOGGER.info(String.format("Attempting to write output directory %s.", outputFile.getAbsolutePath()));

                    if (!outputFile.exists()) {
                        LOGGER.info(String.format("Attempting to create output directory %s.", outputFile.getAbsolutePath()));
                        Files.createDirectories(outputFile.toPath());
                    }
                } else {
                    LOGGER.info(String.format("Creating output file %s.", outputFile.getAbsolutePath()));
                    try (final OutputStream outputFileStream = new FileOutputStream(outputFile)) {
                        IOUtils.copy(debInputStream, outputFileStream);
                    }
                }
                uncompressedFiles.add(outputFile);

                stateCallback.apply(new ProgressStateDTO.Builder()
                                .withPercent((double) debInputStream.getBytesRead() / (double) finalSize * 100)
                                .withProgressText("Extracting " + outputFile.getName())
                                .build()
                );

            }
        } catch (IOException | org.apache.commons.compress.archivers.ArchiveException e) {
            throw new ArchiveException("Unable to extract the file", e);
        }

        return uncompressedFiles;
    }


    /**
     * Gunzip a file
     * @param inputFile source file
     * @param outputFile destionation file
     * @return the destionation file
     * @throws ArchiveException if any error occurs
     */
    public File gunzip(final File inputFile, final File outputFile) throws ArchiveException {
        LOGGER.info(String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputFile.getAbsolutePath()));

        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputFile));
             FileOutputStream out = new FileOutputStream(outputFile)) {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new ArchiveException("Unable to gunzip file", e);
        }
        return outputFile;
    }

    /**
     * Bunzip2 a file
     * @param inputFile source file
     * @param outputFile destionation file
     * @return the destionation file
     * @throws ArchiveException if any error occurs
     */
    public File bunzip2(final File inputFile, final File outputFile) throws ArchiveException {
        LOGGER.info(String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputFile.getAbsolutePath()));

        try (BZip2CompressorInputStream in = new BZip2CompressorInputStream(new FileInputStream(inputFile));
             FileOutputStream out = new FileOutputStream(outputFile)) {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new ArchiveException("Unable to gunzip file", e);
        }
        return outputFile;
    }



}
