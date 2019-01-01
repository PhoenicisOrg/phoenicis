package org.phoenicis.tools.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

    /**
     * Creates the instance. Stores internally the {@link URL} and the {@link java.net.URLConnection} objects
     *
     * @param urlConnection The URL connection
     * @param url           The URL
     * @see #fromURL(URL) to build
     */
    private PhoenicisUrlConnection(HttpURLConnection urlConnection, URL url) {
        this.delegateUrlConnexion = urlConnection;
        this.url = url;
        this.responseCode = null;
    }

    /**
     * Constructor Equivalents to {@link URL#openConnection()}
     *
     * @param url The URL
     * @return The {@link PhoenicisUrlConnection} instance
     * @throws IOException if any IO Error happens
     */
    static PhoenicisUrlConnection fromURL(URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);

        return new PhoenicisUrlConnection(
                connection,
                url);
    }

    /**
     * Initialize the connection
     *
     * @throws IOException if any IO error happens
     */
    void connect() throws IOException {
        this.followRedirectsAndGetResponseCode();
    }

    /**
     * Get last modified header
     *
     * @return last modified
     */
    long getLastModified() {
        return delegateUrlConnexion.getLastModified();
    }

    /**
     * Get connection input stream
     *
     * @return The connection input stream
     * @throws IOException if any IO error happens
     */
    InputStream getInputStream() throws IOException {
        return delegateUrlConnexion.getInputStream();
    }

    /**
     * Returns the value of the content-length header field as long
     *
     * @return the content-length of the resource
     */
    long getContentLengthLong() {
        return delegateUrlConnexion.getContentLengthLong();
    }

    /**
     * Gets the filename of the remote resource. Follows HTTP redirects if this is required
     * and reads content-disposition header if this is possible. Otherwise, it will parse
     * the URL
     *
     * @return The filename of the remote resource
     * @throws IOException if any IO error happens
     */
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

    /**
     * Gets HTTP response code
     * @return http response code
     * @throws IOException if any IO error happens
     */
    private int getReponseCode() throws IOException {
        return this.followRedirectsAndGetResponseCode();
    }

    /**
     * Determines the filename from the URL
     * @return the filename
     */
    private String fetchFilenameFromUrl() {
        return url.toExternalForm().substring(url.toExternalForm().lastIndexOf("/") + 1).split("\\?")[0];
    }

    /**
     * Gets the response code after having follow HTTP redirects
     * @return The Http response code
     * @throws IOException if any IO error happens
     */
    private int followRedirectsAndGetResponseCode() throws IOException {
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
            followRedirectsAndGetResponseCode();
        }

        return responseCode;
    }

    /**
     * Sets HTTP headers
     * @param headers Headers to be set
     */
    void setHeaders(Map<String, String> headers) {
        this.headers = headers;
        headers.forEach(delegateUrlConnexion::setRequestProperty);
    }
}
