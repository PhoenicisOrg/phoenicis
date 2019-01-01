package org.phoenicis.tools.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Wrapper around {@link HttpURLConnection class} that follows correctly HTTP redirects
 */
class PhoenicisUrlConnection {
    private Integer responseCode;
    private HttpURLConnection delegateUrlConnexion;
    private URL url;
    private Map<String, String> headers;

    private PhoenicisUrlConnection(HttpURLConnection urlConnection, URL url) {
        this.delegateUrlConnexion = urlConnection;
        this.url = url;
        this.responseCode = null;
    }

    static PhoenicisUrlConnection fromURL(URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);

        return new PhoenicisUrlConnection(
                connection,
                url);
    }

    void connect() throws IOException {
        this.followRedirectsAndGetReponseCode();
    }

    long getLastModified() {
        return delegateUrlConnexion.getLastModified();
    }

    InputStream getInputStream() throws IOException {
        return delegateUrlConnexion.getInputStream();
    }

    long getContentLengthLong() {
        return delegateUrlConnexion.getContentLengthLong();
    }

    String fetchFileName() throws IOException {
        int responseCode = this.getReponseCode();

        if (responseCode == 200) {
            final String disposition = delegateUrlConnexion.getHeaderField("Content-Disposition");
            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    return disposition.substring(index + 10, disposition.length() - 1);
                }
            }
        }

        return fetchFilenameFromUrl();
    }

    private int getReponseCode() throws IOException {
        return this.followRedirectsAndGetReponseCode();
    }

    private String fetchFilenameFromUrl() {
        return url.toExternalForm().substring(url.toExternalForm().lastIndexOf("/") + 1).split("\\?")[0];
    }

    void setRequestMethod(String method) throws ProtocolException {
        this.delegateUrlConnexion.setRequestMethod(method);
    }

    private int followRedirectsAndGetReponseCode() throws IOException {
        if (responseCode == null) {
            responseCode = delegateUrlConnexion.getResponseCode();
        }

        if (responseCode == 302 || responseCode == 301) {
            final String redirectLocation = delegateUrlConnexion.getHeaderField("Location");
            this.url = new URL(redirectLocation);
            this.delegateUrlConnexion = (HttpURLConnection) url.openConnection();
            this.delegateUrlConnexion.setInstanceFollowRedirects(false);
            this.responseCode = null;
            if (this.headers != null) {
                setHeaders(headers);
            }
            followRedirectsAndGetReponseCode();
        }

        return responseCode;
    }

    void setHeaders(Map<String, String> headers) {
        this.headers = headers;
        headers.forEach(delegateUrlConnexion::setRequestProperty);
    }
}
