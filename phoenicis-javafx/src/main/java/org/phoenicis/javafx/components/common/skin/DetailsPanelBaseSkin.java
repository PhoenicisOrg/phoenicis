package org.phoenicis.javafx.components.common.skin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.phoenicis.javafx.components.common.control.DetailsPanelBase;

import java.util.Optional;

/**
 * The base skin for all details panels
 *
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
public abstract class DetailsPanelBaseSkin<C extends DetailsPanelBase<C, S>, S extends DetailsPanelBaseSkin<C, S>>
        extends SkinBase<C, S> {
    /**
     * The title of the details panel
     */
    protected final StringProperty title;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    protected DetailsPanelBaseSkin(C control) {
        super(control);

        this.title = new SimpleStringProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("detailsPane");

        container.setTop(createHeader());
        container.setCenter(createContent());

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
        titleLabel.textProperty().bind(title);

        final Button closeButton = new Button();
        closeButton.getStyleClass().add("closeIcon");
        closeButton.setOnAction(event -> getOnClose().ifPresent(Runnable::run));

        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        return new HBox(titleLabel, filler, closeButton);
    }

    /**
     * Creates the content for the details panel
     *
     * @return The content
     */
    protected abstract Node createContent();

    private Optional<Runnable> getOnClose() {
        return Optional.ofNullable(getControl().getOnClose());
    }
}
