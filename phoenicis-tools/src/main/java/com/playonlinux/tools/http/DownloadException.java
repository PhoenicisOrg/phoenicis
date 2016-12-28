package com.playonlinux.tools.http;

public class DownloadException extends RuntimeException {
    public DownloadException(String message, Exception e) {
        super(message, e);
    }
}
