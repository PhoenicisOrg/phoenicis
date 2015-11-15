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

package com.playonlinux.core.utils.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.common.io.CountingInputStream;
import com.playonlinux.core.entities.ProgressStateEntity;


/**
 * Tar extraction utilities
 */
public class Tar  {
    private static final Logger LOGGER = Logger.getLogger(Tar.class);
    private static final String TAR_ERROR_MESSAGE = "Unable to open input stream";

    List<File> uncompressTarBz2File(File inputFile, File outputDir, Function<ProgressStateEntity, Void> stateCallback) throws ArchiveException {
        try(CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile)) ;
            InputStream inputStream = new BZip2CompressorInputStream(countingInputStream)) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(inputStream, countingInputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    List<File> uncompressTarGzFile(File inputFile, File outputDir, Function<ProgressStateEntity, Void> stateCallback) throws ArchiveException {
        try(CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile)) ;
            InputStream inputStream = new GZIPInputStream(countingInputStream)) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(inputStream, countingInputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    List<File> uncompressTarXzFile(File inputFile, File outputDir, Function<ProgressStateEntity, Void> stateCallback) throws ArchiveException {
        try(CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile)) ;
            InputStream inputStream = new XZCompressorInputStream(countingInputStream)) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(inputStream, countingInputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    List<File> uncompressTarFile(File inputFile, File outputDir, Function<ProgressStateEntity, Void> stateCallback) throws ArchiveException {
        try(CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile))) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(countingInputStream, countingInputStream, outputDir, finalSize, stateCallback);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    /**
     * Uncompress a tar
     *
     * @param countingInputStream to count the number of byte extracted
     * @param outputDir The directory where files should be extracted
     * @return A list of extracted files
     * @throws ArchiveException if the process fails
     */
    private List<File> uncompress(final InputStream inputStream, CountingInputStream countingInputStream, final File outputDir, long finalSize,
                                  Function<ProgressStateEntity, Void> stateCallback) throws ArchiveException {
        final List<File> uncompressedFiles = new LinkedList<>();
        try(ArchiveInputStream debInputStream = new ArchiveStreamFactory().createArchiveInputStream("tar", inputStream)) {
            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
                final File outputFile = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    LOGGER.info(String.format("Attempting to write output directory %s.", outputFile.getAbsolutePath()));

                    if (!outputFile.exists()) {
                        LOGGER.info(String.format("Attempting to createPrefix output directory %s.", outputFile.getAbsolutePath()));
                        Files.createDirectories(outputFile.toPath());
                    }
                } else {
                    LOGGER.info(String.format("Creating output file %s (%s).", outputFile.getAbsolutePath(), entry.getMode()));

                    if(entry.isSymbolicLink()) {
                        Files.createSymbolicLink(Paths.get(outputFile.getAbsolutePath()), Paths.get(entry.getLinkName()));
                    } else {
                        try (final OutputStream outputFileStream = new FileOutputStream(outputFile)) {
                            IOUtils.copy(debInputStream, outputFileStream);

                            Files.setPosixFilePermissions(Paths.get(outputFile.getPath()),
                                    com.playonlinux.core.utils.Files.octToPosixFilePermission(
                                            entry.getMode()
                                    )
                            );
                        }
                    }

                }
                uncompressedFiles.add(outputFile);

                stateCallback.apply(new ProgressStateEntity.Builder()
                                .withPercent((double) countingInputStream.getCount() / (double) finalSize * (double) 100)
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
