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
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

/**
 * Tar extraction utilities
 */
@Safe
public class Tar {
    private static final String TAR_ERROR_MESSAGE = "Unable to open input stream";
    private final Logger LOGGER = LoggerFactory.getLogger(Tar.class);
    private final FileUtilities fileUtilities;

    public Tar(FileUtilities fileUtilities) {
        this.fileUtilities = fileUtilities;
    }

    List<File> uncompressTarBz2File(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        return uncompressTarBz2File(inputFile, outputDir, stateCallback, true);
    }

    List<File> uncompressTarBz2File(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback,
            boolean trackExtractedFiles) {
        try (CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile));
                InputStream inputStream = new BZip2CompressorInputStream(countingInputStream)) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(inputStream, countingInputStream, outputDir, finalSize, stateCallback,
                    trackExtractedFiles);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    List<File> uncompressTarGzFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        return uncompressTarGzFile(inputFile, outputDir, stateCallback, true);
    }

    List<File> uncompressTarGzFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback,
            boolean trackExtractedFiles) {
        try (CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile));
                InputStream inputStream = new GZIPInputStream(countingInputStream)) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(inputStream, countingInputStream, outputDir, finalSize, stateCallback,
                    trackExtractedFiles);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    List<File> uncompressTarXzFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        return uncompressTarXzFile(inputFile, outputDir, stateCallback, true);
    }

    List<File> uncompressTarXzFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback,
            boolean trackExtractedFiles) {
        try (CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile));
                InputStream inputStream = new XZCompressorInputStream(countingInputStream)) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(inputStream, countingInputStream, outputDir, finalSize, stateCallback,
                    trackExtractedFiles);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    List<File> uncompressTarFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        return uncompressTarFile(inputFile, outputDir, stateCallback, true);
    }

    List<File> uncompressTarFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback,
            boolean trackExtractedFiles) {
        try (CountingInputStream countingInputStream = new CountingInputStream(new FileInputStream(inputFile))) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            return uncompress(countingInputStream, countingInputStream, outputDir, finalSize, stateCallback,
                    trackExtractedFiles);
        } catch (IOException e) {
            throw new ArchiveException(TAR_ERROR_MESSAGE, e);
        }
    }

    /**
     * Gunzip a file
     *
     * @param inputFile
     *            source file
     * @param outputFile
     *            destionation file
     * @return the destionation file
     * @throws ArchiveException
     *             if any error occurs
     */
    public File gunzip(final File inputFile, final File outputFile) {
        LOGGER.info(
                String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputFile.getAbsolutePath()));

        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputFile));

                FileOutputStream out = new FileOutputStream(outputFile)) {
            IOUtils.copy(in, out);
            return outputFile;
        } catch (IOException e) {
            throw new ArchiveException("Unable to gunzip file", e);
        }
    }

    /**
     * Bunzip2 a file
     *
     * @param inputFile
     *            source file
     * @param outputFile
     *            destionation file
     * @return the destionation file
     * @throws ArchiveException
     *             if any error occurs
     */
    public File bunzip2(final File inputFile, final File outputFile) {
        LOGGER.info(
                String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputFile.getAbsolutePath()));
        try (BZip2CompressorInputStream in = new BZip2CompressorInputStream(new FileInputStream(inputFile));
                FileOutputStream out = new FileOutputStream(outputFile)) {
            IOUtils.copy(in, out);
            return outputFile;
        } catch (IOException e) {
            throw new ArchiveException("Unable to gunzip file", e);
        }
    }

    /**
     * Uncompress a tar
     *
     * @param countingInputStream
     *            to count the number of byte extracted
     * @param outputDir
     *            The directory where files should be extracted
     * @return A list of extracted files
     * @throws ArchiveException
     *             if the process fails
     */
    private List<File> uncompress(final InputStream inputStream, CountingInputStream countingInputStream,
            final File outputDir, long finalSize, Consumer<ProgressEntity> stateCallback,
            boolean trackExtractedFiles) {
        final List<File> uncompressedFiles = new ArrayList<>();
        int extractedEntries = 0;
        int lastNotifiedPercent = -1;

        try (ArchiveInputStream debInputStream = new ArchiveStreamFactory().createArchiveInputStream("tar",
                inputStream)) {
            TarArchiveEntry entry;
            while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
                extractedEntries++;
                final File outputFile = new File(outputDir, entry.getName());

                if (entry.isDirectory()) {
                    LOGGER.debug("Attempting to write output directory {}.", outputFile.getAbsolutePath());

                    if (!outputFile.exists()) {
                        LOGGER.debug("Attempting to create output directory {}.", outputFile.getAbsolutePath());
                        Files.createDirectories(outputFile.toPath());
                    }
                } else {
                    LOGGER.debug("Creating output file {} ({}).", outputFile.getAbsolutePath(), entry.getMode());

                    if (entry.isSymbolicLink()) {
                        Files.createSymbolicLink(Paths.get(outputFile.getAbsolutePath()),
                                Paths.get(entry.getLinkName()));
                    } else {
                        try (final OutputStream outputFileStream = new FileOutputStream(outputFile)) {
                            IOUtils.copy(debInputStream, outputFileStream);

                            Files.setPosixFilePermissions(Paths.get(outputFile.getPath()),
                                    fileUtilities.octToPosixFilePermission(entry.getMode()));
                        }
                    }
                }

                if (trackExtractedFiles) {
                    uncompressedFiles.add(outputFile);
                }

                final double percent = (double) countingInputStream.getCount() / (double) finalSize * 100d;
                final int roundedPercent = (int) percent;
                if (roundedPercent > lastNotifiedPercent || extractedEntries % 500 == 0) {
                    stateCallback.accept(new ProgressEntity.Builder().withPercent(percent)
                            .withProgressText("Extracting " + outputFile.getName()).build());
                    lastNotifiedPercent = roundedPercent;
                }

                if (extractedEntries % 1000 == 0) {
                    LOGGER.info("Extracted {} entries from archive...", extractedEntries);
                }
            }

            stateCallback.accept(new ProgressEntity.Builder().withPercent(100d).withProgressText("Extraction complete")
                    .build());
            LOGGER.info("Finished extracting {} entries to {}.", extractedEntries, outputDir.getAbsolutePath());

            return uncompressedFiles;
        } catch (IOException | org.apache.commons.compress.archivers.ArchiveException e) {
            throw new ArchiveException("Unable to extract the file", e);
        }
    }

}
