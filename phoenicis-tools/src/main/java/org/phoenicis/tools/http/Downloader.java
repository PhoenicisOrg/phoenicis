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

package org.phoenicis.tools.http;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.phoenicis.entities.ProgressState;
import org.phoenicis.tools.files.FileSizeUtilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

/**
 * file download utilities (mainly to be used from JS)
 */
@Safe
public class Downloader {
    private static final String EXCEPTION_ITEM_DOWNLOAD_FAILED = "Download of %s has failed";
    private static final int BLOCK_SIZE = 1024;

    private final FileSizeUtilities fileSizeUtilities;

    /**
     * constructor
     * @param fileSizeUtilities
     */
    public Downloader(FileSizeUtilities fileSizeUtilities) {
        this.fileSizeUtilities = fileSizeUtilities;
    }

    /**
     * downloads url to localFile, shows progress via onChange
     * @param url download URL
     * @param localFile destination of the download
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     */
    public void get(String url, String localFile, Consumer<ProgressEntity> onChange) {
        try {
            get(new URL(url), new File(localFile), onChange);
        } catch (MalformedURLException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }

    /**
     * downloads url to localFile, shows progress via onChange
     * @param url download URL
     * @param localFile destination of the download
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     */
    public void get(URL url, File localFile, Consumer<ProgressEntity> onChange) {
        try {
            get(url, new FileOutputStream(localFile), onChange);
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }

    /**
     * downloads url and returns downloaded content, shows progress via onChange
     * @param url download URL
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     * @return downloaded content
     */
    public String get(String url, Consumer<ProgressEntity> onChange) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            get(new URL(url), outputStream, onChange);
        } catch (MalformedURLException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
        return outputStream.toString();
    }

    /**
     * downloads url and returns downloaded content, shows progress via onChange
     * @param url download URL
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     * @return downloaded content
     */
    public String get(URL url, Consumer<ProgressEntity> onChange) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(url, outputStream, onChange);
        return outputStream.toString();
    }

    /**
     * downloads url and returns downloaded content, shows progress via onChange
     * @param url download URL
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     * @return downloaded content
     */
    public byte[] getBytes(URL url, Consumer<ProgressEntity> onChange) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(url, outputStream, onChange);
        return outputStream.toByteArray();
    }

    /**
     * downloads url to outputStream, shows progress via onChange
     * @param url download URL
     * @param outputStream file is downloaded to this stream
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     */
    private void get(URL url, OutputStream outputStream, Consumer<ProgressEntity> onChange) {
        try {
            URLConnection connection = url.openConnection();
            saveConnectionToStream(url, connection, outputStream, onChange);
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }

    /**
     * downloads url to outputStream, shows progress via onChange
     * @param url download URL
     * @param connection URLConnection which is used for the download
     * @param outputStream file is downloaded to this stream
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     */
    private void saveConnectionToStream(URL url, URLConnection connection, OutputStream outputStream,
            Consumer<ProgressEntity> onChange) {
        float percentage = 0F;
        changeState(ProgressState.READY, percentage, "", onChange);

        try (BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BLOCK_SIZE)) {
            long fileSize = connection.getContentLengthLong();

            byte[] data = new byte[BLOCK_SIZE];
            int i;
            long totalDataRead = 0L;
            while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
                totalDataRead += i;
                bufferedOutputStream.write(data, 0, i);

                percentage = ((float) totalDataRead * 100) / ((float) fileSize);

                changeState(ProgressState.PROGRESSING, percentage,
                        String.format("%s / %s downloaded",
                                fileSizeUtilities.humanReadableByteCount(totalDataRead, false),
                                fileSizeUtilities.humanReadableByteCount(fileSize, false)),
                        onChange);

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("The download has been aborted");
                }
            }

            outputStream.flush();
        } catch (IOException | InterruptedException e) {
            changeState(ProgressState.FAILED, percentage, "", onChange);
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }

        changeState(ProgressState.SUCCESS, percentage, "", onChange);
    }

    /**
     * updates the progress indicator
     * @param state current state (e.g. if download finished)
     * @param percentage progress percentage
     * @param progressText e.g. downloaded x of y bytes
     * @param onChange consumer to show the download progress (e.g. a progress bar)
     */
    private void changeState(ProgressState state, float percentage, String progressText,
            Consumer<ProgressEntity> onChange) {
        if (onChange != null) {
            ProgressEntity currentState = new ProgressEntity.Builder().withPercent(percentage).withState(state)
                    .withProgressText(progressText).build();
            onChange.accept(currentState);
        }
    }

    /**
     * checks if the downloadable file has been updated more recently than localFile
     * This can be used to avoid re-downloading existing resources if no new version is available.
     * Note: The last modified date must be set correctly by the server.
     * @param localFile local file (should have been downloaded from url)
     * @param url download URL
     * @return update for localFile is available
     */
    public boolean isUpdateAvailable(String localFile, String url) {
        try {
            return isUpdateAvailable(new File(localFile), new URL(url));
        } catch (MalformedURLException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }

    /**
     * checks if the downloadable file has been updated more recently than localFile
     * This can be used to avoid re-downloading existing resources if no new version is available.
     * Note: The last modified date must be set correctly by the server.
     * @param localFile local file (should have been downloaded from url)
     * @param url download URL
     * @return update for localFile is available
     */
    public boolean isUpdateAvailable(File localFile, URL url) {
        if (!localFile.exists()) {
            return true;
        }
        try {
            URLConnection connection = url.openConnection();
            connection.connect();
            long fileLastModified = localFile.lastModified();
            long urlLastModified = connection.getLastModified();
            if (fileLastModified == 0 || urlLastModified == 0) {
                // we know nothing
                return true;
            }
            return localFile.lastModified() <= connection.getLastModified();
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }
}
