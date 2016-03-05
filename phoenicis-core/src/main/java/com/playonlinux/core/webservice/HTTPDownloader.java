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

package com.playonlinux.core.webservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

import com.playonlinux.core.entities.ProgressEntity;
import com.playonlinux.core.entities.ProgressState;

import lombok.Setter;

public class HTTPDownloader {
    private static final String EXCEPTION_ITEM_DOWNLOAD_FAILED = "Download of %s has failed";

    private static final int BLOCK_SIZE = 1024;

    private final URL url;
    @Setter
    private Consumer<ProgressEntity> onChange;

    public HTTPDownloader(URL url) {
        this.url = url;
    }

    public void get(File localFile) {
        try {
            get(new FileOutputStream(localFile));
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, this.url), e);
        }
    }

    public String get() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(outputStream);
        return outputStream.toString();
    }

    public byte[] getBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(outputStream);
        return outputStream.toByteArray();
    }

    private void get(OutputStream outputStream) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            saveConnectionToStream(connection, outputStream);
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, this.url), e);
        }
    }

    private void saveConnectionToStream(HttpURLConnection connection, OutputStream outputStream) {
        float percentage = 0F;
        changeState(ProgressState.READY, percentage);

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BLOCK_SIZE)) {
            long fileSize = connection.getContentLengthLong();

            byte[] data = new byte[BLOCK_SIZE];
            int i;
            long totalDataRead = 0L;
            while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
                totalDataRead += i;
                bufferedOutputStream.write(data, 0, i);

                percentage = totalDataRead * 100 / fileSize;
                changeState(ProgressState.PROGRESSING, percentage);

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("The download has been aborted");
                }
            }

            outputStream.flush();
        } catch (IOException | InterruptedException e) {
            changeState(ProgressState.FAILED, percentage);
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, this.url), e);
        }

        changeState(ProgressState.SUCCESS, percentage);
    }

    private void changeState(ProgressState state, float percentage) {
        if (onChange != null) {
            ProgressEntity currentState = new ProgressEntity.Builder().withPercent(percentage).withState(state).build();
            onChange.accept(currentState);
        }
    }
}