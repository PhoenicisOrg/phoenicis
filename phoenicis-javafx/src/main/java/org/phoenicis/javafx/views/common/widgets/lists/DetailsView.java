package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * A closable details view
 *
 * @author marc
 * @since 21.05.17
 */
public abstract class DetailsView extends BorderPane {
    /**
     * The method to be called, when this details view is closed
     */
    private Runnable onClose;

    private Button closeButton;

    private Label titleLabel;

    /**
     * Constructor
     */
    public DetailsView() {
        super();

        this.getStyleClass().add("detailsPane");

        this.populateHeader();
    }

    /**
     * Populate the header with a label for the title of this details view and a close button
     */
    private void populateHeader() {
        this.titleLabel = new Label();
        this.titleLabel.getStyleClass().add("descriptionTitle");

        this.closeButton = new Button();
        this.closeButton.getStyleClass().add("closeIcon");
        this.closeButton.setOnAction(event -> onClose.run());

        Region filler = new Region();

        HBox headerBox = new HBox();
        headerBox.getChildren().setAll(titleLabel, filler, closeButton);
        HBox.setHgrow(filler, Priority.ALWAYS);

        this.setTop(headerBox);
    }

    /**
     * Update the title of this details view to the given string
     *
     * @param title The new title of this details view
     */
    protected void setTitle(String title) {
        this.titleLabel.setText(title);
    }

    /**
     * Update the consumer that is called when this details view is closed
     *
     * @param onClose The method to be called when this details view is closed
     */
    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
}
