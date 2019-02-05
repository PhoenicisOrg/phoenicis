package org.phoenicis.javafx.components.common.skin;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.phoenicis.javafx.components.common.control.DetailsPanel;

import java.util.Optional;

/**
 * A skin implementation belonging to the {@link DetailsPanel} component
 */
public class DetailsPanelSkin extends SkinBase<DetailsPanel, DetailsPanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public DetailsPanelSkin(DetailsPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("detailsPane");

        container.setTop(createHeader());

        container.centerProperty().bind(getControl().contentProperty());

        getChildren().addAll(container);
    }

    /**
     * Creates the header for the details panel.
     * The header contains an optional title and a close button
     *
     * @return The header
     */
    private HBox createHeader() {
        final Label titleLabel = new Label();
        titleLabel.getStyleClass().add("descriptionTitle");
        titleLabel.textProperty().bind(getControl().titleProperty());

        final Button closeButton = new Button();
        closeButton.getStyleClass().add("closeIcon");
        closeButton.setOnAction(event -> Optional.ofNullable(getControl().getOnClose()).ifPresent(Runnable::run));

        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        return new HBox(titleLabel, filler, closeButton);
    }
}
