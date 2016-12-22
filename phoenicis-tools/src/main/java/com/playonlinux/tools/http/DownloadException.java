package com.playonlinux.tools.http;

import java.io.IOException;

public class DownloadException extends RuntimeException {
    public DownloadException(String message, Exception e) {
        super(message, e);
    }
}
