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

package org.phoenicis.javafx.views.setupwindow;

import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.phoenicis.scripts.ui.Message;

import java.net.URL;

public class StepRepresentationHtmlPresentation extends AbstractStepRepresentation {
    private final String htmlToShow;

    public StepRepresentationHtmlPresentation(SetupUiJavaFXImplementation parent, Message<?> message, String htmlToShow) {
        super(parent, message);
        this.htmlToShow = htmlToShow;
    }

    @Override
    protected void drawStepContent() {
        final String title = this.getParentWizardTitle();

        VBox contentPane = new VBox();
        contentPane.setId("presentationBackground");

        Label titleWidget = new Label(title + "\n\n");
        titleWidget.setId("presentationTextTitle");

        WebView webView = new WebView();
        VBox.setVgrow(webView, Priority.ALWAYS);

        webView.getEngine().loadContent(htmlToShow);
        final URL style = getClass().getResource(String.format("/org/phoenicis/javafx/themes/%s/description.css", getParent().getThemeManager().getCurrentTheme().getShortName()));
        webView.getEngine().setUserStyleSheetLocation(style.toString());

        contentPane.getChildren().addAll(webView);

        getParent().getRoot().setCenter(contentPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> getMessageAwaitingForResponse().send(null));
    }

}
