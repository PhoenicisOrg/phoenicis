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

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.phoenicis.tools.files.FileAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

@Safe
public class Extractor {
    private final Logger LOGGER = LoggerFactory.getLogger(Tar.class);
    private final FileAnalyser fileAnalyser;
    private final Tar tar;
    private final Zip zip;

    public Extractor(FileAnalyser fileAnalyser, Tar tar, Zip zip) {
        this.fileAnalyser = fileAnalyser;
        this.tar = tar;
        this.zip = zip;
    }

    public List<File> uncompress(String inputFile, String outputDir, Consumer<ProgressEntity> onChange) {
        return uncompress(new File(inputFile), new File(outputDir), onChange);
    }

    public void uncompressWithoutTracking(String inputFile, String outputDir, Consumer<ProgressEntity> onChange) {
        uncompress(new File(inputFile), new File(outputDir), onChange, false);
    }

    /**
     * Uncompress a .tar file
     *
     * @param inputFile input file
     * @param outputDir output directory
     * @return list of uncompressed files
     */
    public List<File> uncompress(File inputFile, File outputDir, Consumer<ProgressEntity> onChange) {
        return uncompress(inputFile, outputDir, onChange, true);
    }

    public void uncompressWithoutTracking(File inputFile, File outputDir, Consumer<ProgressEntity> onChange) {
        uncompress(inputFile, outputDir, onChange, false);
    }

    private List<File> uncompress(File inputFile, File outputDir, Consumer<ProgressEntity> onChange,
            boolean trackExtractedFiles) {
        LOGGER.info(
                String.format("Uncompressing %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

        switch (fileAnalyser.getMimetype(inputFile)) {
            case "application/x-bzip2":
                return tar.uncompressTarBz2File(inputFile, outputDir, onChange, trackExtractedFiles);
            case "application/x-gzip":
                return tar.uncompressTarGzFile(inputFile, outputDir, onChange, trackExtractedFiles);
            case "application/x-xz":
                return tar.uncompressTarXzFile(inputFile, outputDir, onChange, trackExtractedFiles);
            case "application/zip":
            case "application/x-dosexec":
                return zip.uncompressZipFile(inputFile, outputDir, onChange, trackExtractedFiles);
            default:
                return tar.uncompressTarFile(inputFile, outputDir, onChange, trackExtractedFiles);
        }

    }
}