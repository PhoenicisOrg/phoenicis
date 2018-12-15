package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.phoenicis.javafx.components.common.control.CompactListElement;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;

import java.util.ArrayList;
import java.util.List;

/**
 * The skin for the {@link CompactListElement} component
 *
 * @param <E> The concrete type of the element shown in this list element
 */
public class CompactListElementSkin<E> extends SkinBase<CompactListElement<E>, CompactListElementSkin<E>> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public CompactListElementSkin(CompactListElement<E> control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final GridPane container = new GridPane();
        container.getStyleClass().add("compactListElement");

        List<ColumnConstraints> constraints = new ArrayList<>();

        // add the miniature icon
        container.add(createMiniature(), 0, 0);

        constraints.add(new ColumnConstraints());

        // add the title label
        container.add(createTitle(), 1, 0);

        constraints.add(new ColumnConstraintsWithPercentage(40));

        // add the additional information
        getControl().getAdditionalInformation().forEach(information -> {
            final Label informationLabel = new Label(information.getContent());
            informationLabel.getStyleClass().add("information");

            container.add(informationLabel, constraints.size(), 0);

            constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));
        });

        // set the last constraint to fill the remaining space
        constraints.set(constraints.size() - 1, new ColumnConstraints());

        container.getColumnConstraints().setAll(constraints);

        getChildren().addAll(container);
    }

    /**
     * Creates a miniature icon for the item
     * @return A miniature icon for the item
     */
    private Region createMiniature() {
        final Region icon = new Region();

        icon.getStyleClass().add("compactListMiniatureImage");
        icon.styleProperty().bind(
                Bindings.createStringBinding(
                        () -> String.format("-fx-background-image: url(\"%s\");",
                                getControl().getMiniatureUri().toString()),
                        getControl().miniatureUriProperty()));

        return icon;
    }

    /**
     * Creates a label for the title of the list element
     *
     * @return A label with the title of the list element
     */
    private Label createTitle() {
        final Label titleLabel = new Label();

        titleLabel.getStyleClass().add("information");
        titleLabel.textProperty().bind(getControl().titleProperty());

        return titleLabel;
    }
}
