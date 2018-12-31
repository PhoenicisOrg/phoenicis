package org.phoenicis.scripts.ui;

/**
 * Reprents a component that can control an abstract browser view
 */
public interface BrowserControl {
    /**
     * Makes the browser go to a given URL
     * @param url The URL
     */
    void goToUrl(String url);

    /**
     * Wait for a given url to be loaded
     * @param urlMatch The URL to wait for
     */
    void waitForUrl(String urlMatch);

    /**
     * Wait for the current page to be loaded
     */
    void waitForBeingLoaded();

    /**
     * Get the current browser URL
     * @return the current browser URL
     */
    String getCurrentUrl();
}
