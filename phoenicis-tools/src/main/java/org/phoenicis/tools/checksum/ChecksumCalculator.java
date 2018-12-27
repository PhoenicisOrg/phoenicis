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

package org.phoenicis.tools.checksum;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

@Safe
public class ChecksumCalculator {
    private static final int BLOCK_SIZE = 2048;
    private static final String WAIT_MESSAGE = tr("Verifying the file...");

    public String calculate(String fileToCheck, String algorithm, Consumer<ProgressEntity> onChange)
            throws IOException {
        return calculate(new File(fileToCheck), algorithm, onChange);
    }

    public String calculate(File fileToCheck, String algorithm, Consumer<ProgressEntity> onChange) throws IOException {
        final long fileSize = FileUtils.sizeOf(fileToCheck);
        try (final FileInputStream inputStream = new FileInputStream(fileToCheck)) {
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException(e);
            }

            byte[] digest = getDigest(inputStream, messageDigest, fileSize, onChange);
            return Hex.encodeHexString(digest);
        }
    }

    private byte[] getDigest(InputStream inputStream, MessageDigest messageDigest, long sizeInBytes,
            Consumer<ProgressEntity> onChange) throws IOException {

        messageDigest.reset();
        byte[] bytes = new byte[BLOCK_SIZE];
        int numBytes;
        int readBytes = 0;
        while ((numBytes = inputStream.read(bytes)) != -1) {
            messageDigest.update(bytes, 0, numBytes);
            readBytes += numBytes;
            if (sizeInBytes != 0L) {
                double percentage = (double) readBytes * 100. / (double) sizeInBytes;
                changeState(percentage, onChange);
            }
        }
        return messageDigest.digest();
    }

    private void changeState(double percentage, Consumer<ProgressEntity> onChange) {
        if (onChange != null) {
            onChange.accept(
                    new ProgressEntity.Builder().withPercent(percentage).withProgressText(WAIT_MESSAGE).build());
        }
    }
}
