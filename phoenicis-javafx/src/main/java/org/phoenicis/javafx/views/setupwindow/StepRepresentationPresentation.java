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

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import org.phoenicis.scripts.ui.Message;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class StepRepresentationPresentation extends AbstractStepRepresentation {
    private final Node textToShow;

    public StepRepresentationPresentation(SetupWindowJavaFXImplementation parent, Message<?> message, String textToShow) {
        super(parent, message);

        Text textWidget = new Text(textToShow);
        textWidget.setId("presentationText");

        TextFlow flow = new TextFlow();
        flow.getChildren().add(textWidget);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("presentationScrollPane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(flow);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.textToShow = scrollPane;
    }

    public StepRepresentationPresentation(SetupWindowJavaFXImplementation parent, Message<?> message,
                                          String programName, String programEditor, String applicationHomepage,
                                          String scriptorName, String applicationUserRoot, String applicationName) {
        super(parent, message);

        WebView appDescription = new WebView();
        VBox.setVgrow(appDescription, Priority.ALWAYS);

        final String textToShow = String.format(translate(
                "<body>"
                        + "This wizard will help you install \"%1$s\" on your computer.<br><br>"
                        + "This program was created by: %2$s<br><br>"
                        + "For more information about this program, visit:<br><a href=\"%3$s\">%3$s</a><br><br>"
                        + "This installation program is provided by: %4$s<br><br>"
                        + "<br><br>%1$s will be installed in: %5$s<br><br>"
                        + "%6$s is not responsible for anything that might happen as a result of using"
                        + " these scripts.<br><br>Click Next to start"
                        + "</body>")
                , programName, programEditor, applicationHomepage, scriptorName, applicationUserRoot, applicationName);

        appDescription.getEngine().loadContent(textToShow);
        final URL style = getClass().getResource(String.format("/org/phoenicis/javafx/themes/%s/description.css", parent.getThemeManager().getCurrentTheme().getShortName()));
        appDescription.getEngine().setUserStyleSheetLocation(style.toString());
        this.textToShow = appDescription;
    }

    @Override
    protected void drawStepContent() {
        final String title = this.getParentWizardTitle();

        VBox contentPane = new VBox();
        contentPane.setId("presentationBackground");

        Label titleWidget = new Label(title + "\n\n");
        titleWidget.setId("presentationTextTitle");

        contentPane.getChildren().addAll(textToShow);
        getParent().getRoot().setCenter(contentPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> getMessageAwaitingForResponse().send(null));
    }

}
