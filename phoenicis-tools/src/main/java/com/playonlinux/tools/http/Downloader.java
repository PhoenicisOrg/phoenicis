package com.playonlinux.tools.http;

import com.phoenicis.entities.ProgressEntity;
import com.phoenicis.entities.ProgressState;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

public class Downloader {
    private static final String EXCEPTION_ITEM_DOWNLOAD_FAILED = "Download of %s has failed";

    private static final int BLOCK_SIZE = 1024;

    public void get(String url,
                    String localFile,
                    Consumer<ProgressEntity> onChange) {
        try {
            get(new URL(url), new File(localFile), onChange);
        } catch (MalformedURLException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }


    public void get(URL url,
                    File localFile,
                    Consumer<ProgressEntity> onChange) {
        try {
            get(url, new FileOutputStream(localFile), onChange);
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }

    public String get(String url, Consumer<ProgressEntity> onChange) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            get(new URL(url), outputStream, onChange);
        } catch (MalformedURLException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
        return outputStream.toString();
    }

    public String get(URL url, Consumer<ProgressEntity> onChange) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(url, outputStream, onChange);
        return outputStream.toString();
    }

    public byte[] getBytes(URL url, Consumer<ProgressEntity> onChange) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        get(url, outputStream, onChange);
        return outputStream.toByteArray();
    }

    private void get(URL url, OutputStream outputStream, Consumer<ProgressEntity> onChange) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            saveConnectionToStream(url, connection, outputStream, onChange);
        } catch (IOException e) {
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }
    }

    private void saveConnectionToStream(URL url,
                                        HttpURLConnection connection,
                                        OutputStream outputStream,
                                        Consumer<ProgressEntity> onChange) {
        float percentage = 0F;
        changeState(ProgressState.READY, percentage, onChange);

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
                changeState(ProgressState.PROGRESSING, percentage, onChange);

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("The download has been aborted");
                }
            }

            outputStream.flush();
        } catch (IOException | InterruptedException e) {
            changeState(ProgressState.FAILED, percentage, onChange);
            throw new DownloadException(String.format(EXCEPTION_ITEM_DOWNLOAD_FAILED, url), e);
        }

        changeState(ProgressState.SUCCESS, percentage, onChange);
    }

    private void changeState(ProgressState state, float percentage, Consumer<ProgressEntity> onChange) {
        if(onChange != null){
            ProgressEntity currentState = new ProgressEntity.Builder()
                    .withPercent(percentage)
                    .withState(state)
                    .build();
            onChange.accept(currentState);
        }
    }

}
