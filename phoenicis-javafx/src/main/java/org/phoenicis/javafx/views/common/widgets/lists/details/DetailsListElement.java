package org.phoenicis.javafx.views.common.widgets.lists.details;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc on 15.05.17.
 */
public class DetailsListElement<E> extends GridPane {
    private ListWidgetEntry<E> item;

    private Region icon;
    private Label titleLabel;

    public DetailsListElement(ListWidgetEntry<E> item) {
        super();

        this.item = item;

        this.getStyleClass().add("iconListCell");

        this.titleLabel = new Label(item.getTitle());
        this.titleLabel.setWrapText(true);
        this.titleLabel.getStyleClass().add("information");

        List<ColumnConstraints> constraints = new ArrayList<>();
        constraints.add(new ColumnConstraintsWithPercentage(30));

        this.add(titleLabel, 0, 0);

        item.getAdditionalInformation().ifPresent(additionInformations -> additionInformations.forEach(information -> {
            constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

            Label informationLabel = new Label(information.getContent());
            informationLabel.setWrapText(true);
            informationLabel.getStyleClass().add("information");

            this.add(informationLabel, this.getChildren().size(), 0);
        }));

        item.getDetailedInformation().ifPresent(additionInformations -> additionInformations.forEach(information -> {
            constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

            Label informationLabel = new Label(information.getContent());
            informationLabel.setWrapText(true);
            informationLabel.getStyleClass().add("information");

            this.add(informationLabel, this.getChildren().size(), 0);
        }));

        constraints.set(constraints.size() - 1, new ColumnConstraints());

        this.getColumnConstraints().setAll(constraints);
    }

    public E getElement() {
        return item.getItem();
    }
}
