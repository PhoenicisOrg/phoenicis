package org.phoenicis.javafx.components.common.widgets.details.skin;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.phoenicis.javafx.components.common.widgets.details.control.DetailsListElement;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The skin for the {@link DetailsListElement} component
 *
 * @param <E> The concrete type of the element shown in this list element
 */
public class DetailsListElementSkin<E> extends SkinBase<DetailsListElement<E>, DetailsListElementSkin<E>> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public DetailsListElementSkin(DetailsListElement<E> control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final GridPane container = new GridPane();
        container.getStyleClass().add("detailsListElement");

        final List<ColumnConstraints> constraints = new ArrayList<>();

        // add the title label
        container.add(createTitle(), 0, 0);

        constraints.add(new ColumnConstraintsWithPercentage(30));

        // TODO: the skin should react to changes done to the additional information list
        // TODO: the skin should react to changes done to the detailed information list
        Stream.concat(getControl().getAdditionalInformation().stream(), getControl().getDetailedInformation().stream())
                .forEach(information -> {
                    final Label informationLabel = new Label(information.getContent());
                    informationLabel.setWrapText(true);
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
