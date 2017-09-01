package org.phoenicis.javafx.views.common.widgets.lists.compact;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.AdditionalListWidgetInformation;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A list element for a {@link CompactListWidget}
 *
 * @author marc
 * @since 15.05.17
 */
public class CompactListElement<E> extends GridPane {
    /**
     * The item this element contains
     */
    private E item;

    /**
     * The miniature image to be shown inside this element
     */
    private Region icon;

    /**
     * The title label inside this element
     */
    private Label titleLabel;

    /**
     * Constructor
     *
     * @param item                  The item for which a CompactListElement should be created
     * @param miniatureUri          An uri to the miniature which is shown inside this CompactListElement
     * @param title                 The title which is shown inside this CompactListElement
     * @param additionalInformation An optional list of additional information to be shown inside this CompactListElement
     */
    public CompactListElement(E item, URI miniatureUri, String title,
            Optional<List<AdditionalListWidgetInformation>> additionalInformation) {
        super();

        this.item = item;

        this.getStyleClass().add("compactListElement");

        this.icon = new Region();
        this.icon.getStyleClass().add("compactListMiniatureImage");
        this.icon.setStyle(String.format("-fx-background-image: url(\"%s\");", miniatureUri.toString()));

        this.titleLabel = new Label(title);
        this.titleLabel.setWrapText(true);
        this.titleLabel.getStyleClass().add("information");

        List<ColumnConstraints> constraints = new ArrayList<>();
        constraints.add(new ColumnConstraints());
        constraints.add(new ColumnConstraintsWithPercentage(40));

        this.add(icon, 0, 0);
        this.add(titleLabel, 1, 0);

        additionalInformation.ifPresent(additionInformations -> additionInformations.forEach(information -> {
            constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

            Label informationLabel = new Label(information.getContent());
            informationLabel.setWrapText(true);
            informationLabel.getStyleClass().add("information");

            this.add(informationLabel, this.getChildren().size(), 0);
        }));

        constraints.set(constraints.size() - 1, new ColumnConstraints());

        this.getColumnConstraints().setAll(constraints);
    }

    public static <T> CompactListElement<T> create(ListWidgetEntry<T> item) {
        return new CompactListElement<>(item.getItem(), item.getIconUri(), item.getTitle(),
                item.getAdditionalInformation());
    }

    /**
     * Returns the item belonging to this CompactListElement
     *
     * @return The item belonging to this CompactListElement
     */
    public E getElement() {
        return this.item;
    }
}
