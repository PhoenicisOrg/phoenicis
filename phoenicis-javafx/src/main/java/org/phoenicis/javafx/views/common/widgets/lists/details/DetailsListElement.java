package org.phoenicis.javafx.views.common.widgets.lists.details;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.AdditionalListWidgetInformation;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A list element for a {@link DetailsListWidget}
 *
 * @author marc
 * @since 15.05.17
 */
public class DetailsListElement<E> extends GridPane {
    /**
     * The item this element contains
     */
    private E item;

    /**
     * The title label inside this element
     */
    private Label titleLabel;

    /**
     * Constructor
     *
     * @param item                  The item for which a DetailsListElement should be created
     * @param title                 The title which is shown inside this DetailsListElement
     * @param additionalInformation An optional list of additional information to be shown inside this DetailsListElement
     * @param detailedInformation   An optional list of additional detailed information to be shown inside this DetailsListElement
     */
    public DetailsListElement(E item, String title,
            Optional<List<AdditionalListWidgetInformation>> additionalInformation,
            Optional<List<AdditionalListWidgetInformation>> detailedInformation) {
        super();

        this.item = item;

        this.getStyleClass().add("detailsListElement");

        this.titleLabel = new Label(title);
        this.titleLabel.setWrapText(true);
        this.titleLabel.getStyleClass().add("information");

        List<ColumnConstraints> constraints = new ArrayList<>();
        constraints.add(new ColumnConstraintsWithPercentage(30));

        this.add(titleLabel, 0, 0);

        additionalInformation.ifPresent(additionInformations -> additionInformations.forEach(information -> {
            constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

            Label informationLabel = new Label(information.getContent());
            informationLabel.setWrapText(true);
            informationLabel.getStyleClass().add("information");

            this.add(informationLabel, this.getChildren().size(), 0);
        }));

        detailedInformation.ifPresent(additionInformations -> additionInformations.forEach(information -> {
            constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

            Label informationLabel = new Label(information.getContent());
            informationLabel.setWrapText(true);
            informationLabel.getStyleClass().add("information");

            this.add(informationLabel, this.getChildren().size(), 0);
        }));

        constraints.set(constraints.size() - 1, new ColumnConstraints());

        this.getColumnConstraints().setAll(constraints);
    }

    public static <T> DetailsListElement<T> create(ListWidgetEntry<T> item) {
        return new DetailsListElement<>(item.getItem(), item.getTitle(), item.getAdditionalInformation(),
                item.getDetailedInformation());
    }

    /**
     * Returns the item belonging to this DetailsListElement
     *
     * @return The item belonging to this DetailsListElement
     */
    public E getElement() {
        return item;
    }
}
