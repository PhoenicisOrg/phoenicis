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

package org.phoenicis.javafx.views.scriptui;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import org.phoenicis.scripts.ui.BrowserControl;
import org.phoenicis.scripts.ui.Message;

import java.util.concurrent.Semaphore;

/**
 * This step will present a browser to the user
 */
public class StepRepresentationBrowser extends StepRepresentationMessage implements BrowserControl {
    private final WebView webView = new WebView();
    private final Message<?> messageWaitingForResponse;

    public StepRepresentationBrowser(SetupUiJavaFXImplementation parent, Message<?> messageWaitingForResponse,
            String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
        this.messageWaitingForResponse = messageWaitingForResponse;
    }

    @Override
    protected void drawStepContent() {
        super.drawStepContent();
        webView.prefWidthProperty().bind(getContentPane().widthProperty());
        this.addToContentPane(webView);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

    @Override
    public void goToUrl(String url) {
        Platform.runLater(() -> webView.getEngine().load(url));
    }

    @Override
    public void waitForUrl(String urlMatch) {
        final Semaphore lock = new Semaphore(0);

        Platform.runLater(() -> webView.getEngine().getLoadWorker().stateProperty()
                .addListener(((observableValue, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED && urlMatches(getCurrentUrl(), urlMatch)) {
                        lock.release();
                    }
                })));

        try {
            lock.acquire();
        } catch (InterruptedException e) {
            this.messageWaitingForResponse.sendCancelSignal();
        }

    }

    private boolean urlMatches(String currentUrl, String urlMatch) {
        if (currentUrl.equals(urlMatch)) {
            return true;
        }

        if (urlMatch.endsWith("*") && currentUrl.startsWith(urlMatch.replace("*", ""))) {
            return true;
        }

        return false;
    }

    @Override
    public void waitForBeingLoaded() {
        final Semaphore lock = new Semaphore(0);

        Platform.runLater(() -> webView.getEngine().getLoadWorker().stateProperty()
                .addListener(((observableValue, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        lock.release();
                    }
                })));

        try {
            lock.acquire();
        } catch (InterruptedException e) {
            this.messageWaitingForResponse.sendCancelSignal();
        }
    }

    @Override
    public String getCurrentUrl() {
        if (webView.getEngine().getDocument() == null) {
            return "";
        }
        return webView.getEngine().getDocument().getDocumentURI();
    }
}
