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

package com.playonlinux.utils;

import com.playonlinux.ui.api.ProgressControl;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.playonlinux.core.lang.Localisation.translate;

public class Checksum {
    private static final int BLOCK_SIZE = 2048;

    public static String calculate(File fileToCheck, String algorithm) throws NoSuchAlgorithmException, IOException {
        final FileInputStream inputStream = new FileInputStream(fileToCheck);
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

        return Hex.encodeHexString(getDigest(inputStream, messageDigest, null, 0));
    }

    public static String calculate(File fileToCheck, String algorithm, ProgressControl progressControl) throws NoSuchAlgorithmException, IOException {
        final FileInputStream inputStream = new FileInputStream(fileToCheck);
        long sizeInBytes = FileUtils.sizeOf(fileToCheck);
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

        return Hex.encodeHexString(getDigest(inputStream, messageDigest, progressControl, sizeInBytes));
    }

    private static byte[] getDigest(InputStream inputStream, MessageDigest messageDigest,
                                    ProgressControl progressControl, long sizeInBytes)
            throws IOException {

        messageDigest.reset();
        byte[] bytes = new byte[BLOCK_SIZE];
        int numBytes;
        int readBytes = 0;
        while ((numBytes = inputStream.read(bytes)) != -1) {
            messageDigest.update(bytes, 0, numBytes);
            readBytes += numBytes;
            if(progressControl != null && sizeInBytes != 0) {
                double percentage = (double) readBytes / (double) sizeInBytes * 100;
                progressControl.setProgressPercentage(percentage);
                progressControl.setText(translate("Calculating checksum"));
            }
        }
        return messageDigest.digest();
    }

}
