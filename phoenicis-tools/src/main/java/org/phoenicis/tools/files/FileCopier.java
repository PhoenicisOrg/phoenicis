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

import org.phoenicis.entities.ProgressEntity;

import java.io.*;
import java.util.function.Consumer;

public class FileCopier {
    private static final int BLOCK_SIZE = 1024;

    public void copyFile(File sourceFile, File destinationFile, Consumer<ProgressEntity> onChange) throws IOException, InterruptedException {
        int fileSize = (int) sourceFile.length();
        float totalDataRead = 0.0F;

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile),
                     BLOCK_SIZE)) {
            byte[] data = new byte[BLOCK_SIZE];
            int i;
            while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
                totalDataRead += i;
                outputStream.write(data, 0, i);
                int percentCopied = (int) (totalDataRead * 100 / fileSize);

                onChange.accept(new ProgressEntity.Builder().withPercent(percentCopied).build());

                if (Thread.interrupted()) {
                    throw new InterruptedException("The copy process was interrupted");
                }
            }
            inputStream.close();
            outputStream.close();
        }
    }

}
