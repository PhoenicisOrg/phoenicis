package org.phoenicis.scripts.ui;

public interface BrowserControl {
    void goToUrl(String url);

    void waitForUrl(String urlMatch);

    void waitForBeingLoaded();

    String getCurrentUrl();
}
