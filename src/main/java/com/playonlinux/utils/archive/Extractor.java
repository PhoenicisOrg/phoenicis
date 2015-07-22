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

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.dto.ui.ProgressStateDTO;
import com.playonlinux.utils.MimeType;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;

public class Extractor  extends AbstractObservableImplementation<ProgressStateDTO> {
    private static final Logger LOGGER = Logger.getLogger(Tar.class);

    /**
     * Uncompress a .tar file
     * @param inputFile input file
     * @param outputDir output directory
     * @return list of uncompressed files
     * @throws ArchiveException
     */
    public List<File> uncompress(final File inputFile, final File outputDir) throws ArchiveException {
        LOGGER.info(String.format("Uncompressing %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));
        final long finalSize = FileUtils.sizeOf(inputFile);


        try {
            switch (MimeType.getMimetype(inputFile)) {
                case "application/x-bzip2":
                    return new Tar().uncompressTarBz2File(inputFile, outputDir, finalSize, this::changeState);
                case "application/x-gzip":
                    return new Tar().uncompressTarGzFile(inputFile, outputDir, finalSize, this::changeState);
                default:
                    return new Tar().uncompressTarFile(inputFile, outputDir, finalSize, this::changeState);
            }

        } catch (PlayOnLinuxException e) {
            throw new ArchiveException("Unrecognized file format", e);
        }
    }


    private Void changeState(ProgressStateDTO progressStateDTO) {
        this.notifyObservers(progressStateDTO);
        return null;
    }
}
