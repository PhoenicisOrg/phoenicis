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

public class HTTPDownloader {
    private static final String EXCEPTION_ITEM_DOWNLOAD_FAILED = "Download of %s has failed";

    private static final int BLOCK_SIZE = 1024;
    private final URL url;
    private float percentage;
    private Consumer<ProgressEntity> onChange;

    public HTTPDownloader(URL url) {
        this.url = url;
        changeState(ProgressState.READY);
    }

    private HttpURLConnection openConnection(URL remoteFile) throws IOException {
        return (HttpURLConnection) remoteFile.openConnection();
    }

    private void saveConnectionToStream(HttpURLConnection connection, OutputStream outputStream)
            throws DownloadException {
        int fileSize = connection.getContentLength();

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BLOCK_SIZE)) {
            byte[] data = new byte[BLOCK_SIZE];
            int i;
            float totalDataRead = 0.0F;
            while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
                totalDataRead += i;
                bufferedOutputStream.write(data, 0, i);

                this.percentage = totalDataRead * 100 / fileSize;
                changeState(ProgressState.PROGRESSING);

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("The download has been aborted");
                }
            }
        } catch (IOException | InterruptedException e) {
            changeState(ProgressState.FAILED);
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, this.url), e);
        }
        changeState(ProgressState.SUCCESS);
    }

    private void changeState(ProgressState state) {
        if(onChange != null){
            ProgressEntity currentState = new ProgressEntity.Builder().withPercent(this.percentage)
                    .withState(state).build();
            onChange.accept(currentState);   
        }
    }

    public void get(File localFile) throws DownloadException {
        try {
            get(new FileOutputStream(localFile));
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, this.url), e);
        }
    }

    public void get(OutputStream outputStream) throws DownloadException {
        HttpURLConnection connection;
        try {
            connection = openConnection(url);
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, this.url), e);
        }

        this.saveConnectionToStream(connection, outputStream);

    }

    public String get() throws DownloadException {
        return new String(getBytes());
    }

    public byte[] getBytes() throws DownloadException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new DownloadException("Download failed", e);
        }
        return outputStream.toByteArray();
    }

    public void setOnChange(Consumer<ProgressEntity> onChange) {
        this.onChange = onChange;
    }
}