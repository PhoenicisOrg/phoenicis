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

package com.playonlinux.webservice;

import com.playonlinux.common.dto.DownloadStateDTO;
import com.playonlinux.framework.CancelException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

public class HTTPDownloader extends Observable {
    private static final int BLOCK_SIZE = 1024;
    private final URL url;
    private State state;
    private float percentage;

    public enum State {
        READY,
        DOWNLOADING,
        SUCCESS,
        FAILED
    }


    public HTTPDownloader(URL url) {
        this.url = url;
        this.state = State.READY;
        this.changeState();
    }

    private HttpURLConnection openConnection(URL remoteFile) throws IOException {
        return (HttpURLConnection) remoteFile.openConnection();
    }

    private void saveConnectionToStream(HttpURLConnection connection, OutputStream outputStream) throws DownloadException {
        int fileSize = connection.getContentLength();
        float totalDataRead = 0;

        try {
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BLOCK_SIZE);

            byte[] data = new byte[BLOCK_SIZE];
            int i;
            while ((i = inputStream.read(data, 0, BLOCK_SIZE)) >= 0) {
                totalDataRead += i;
                bufferedOutputStream.write(data, 0, i);

                this.percentage = (float) ((totalDataRead * 100.) / fileSize);
                this.state = State.DOWNLOADING;
                this.changeState();

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("The download has been aborted");
                }
            }

            inputStream.close();
            bufferedOutputStream.close();
        } catch(IOException | InterruptedException e) {
            this.state = State.FAILED;
            this.changeState();
            throw new DownloadException(String.format("Download of %s has failed", this.url), e);
        }
        this.state = State.SUCCESS;
        this.changeState();

    }


    private void changeState() {
        this.setChanged();
        DownloadStateDTO currentState = new DownloadStateDTO();
        currentState.setPercent(this.percentage);
        currentState.setState(DownloadStateDTO.State.valueOf(this.state.name()));
        this.notifyObservers(currentState);
    }

    public void get(File localFile) throws DownloadException {
        try {
            get(new FileOutputStream(localFile));
        } catch (FileNotFoundException e) {
            throw new DownloadException(String.format("Download of %s has failed", this.url), e);
        }
    }
    public void get(OutputStream outputStream) throws DownloadException {
        HttpURLConnection connection;
        try {
            connection = openConnection(url);
        } catch (IOException e) {
            throw new DownloadException(String.format("Download of %s has failed", this.url), e);
        }

        this.saveConnectionToStream(connection, outputStream);

    }

    public String get() throws DownloadException {
        OutputStream outputStream = new ByteArrayOutputStream();
        get(outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new DownloadException("Download failed", e);
        }
        return outputStream.toString();

    }
}
