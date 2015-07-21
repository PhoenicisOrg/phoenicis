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

import com.playonlinux.core.observer.AbstractObservableImplementation;
import com.playonlinux.dto.ui.ProgressStateDTO;
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

public class ChecksumCalculator extends AbstractObservableImplementation<ProgressStateDTO> {
    private static final int BLOCK_SIZE = 2048;

    public String calculate(File fileToCheck, String algorithm) throws NoSuchAlgorithmException, IOException {
        final FileInputStream inputStream = new FileInputStream(fileToCheck);
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

        byte[] digest = getDigest(inputStream, messageDigest, FileUtils.sizeOf(fileToCheck));
        this.deleteObservers();
        return Hex.encodeHexString(digest);
    }

    private byte[] getDigest(InputStream inputStream, MessageDigest messageDigest, long sizeInBytes)
            throws IOException {

        messageDigest.reset();
        byte[] bytes = new byte[BLOCK_SIZE];
        int numBytes;
        int readBytes = 0;
        while ((numBytes = inputStream.read(bytes)) != -1) {
            messageDigest.update(bytes, 0, numBytes);
            readBytes += numBytes;
            if(sizeInBytes != 0) {
                double percentage = (double) readBytes / (double) sizeInBytes * 100;
                changeState(percentage);
            }
        }
        return messageDigest.digest();
    }

    private void changeState(double percentage) {
        this.notifyObservers(new ProgressStateDTO.Builder()
                .withPercent(percentage)
                .withProgressText(translate("Please wait while we are verifying the file..."))
                .build()
        );
    }

}
