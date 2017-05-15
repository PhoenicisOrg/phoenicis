package org.phoenicis.javafx.views.common.widgets.lists.compact;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc on 15.05.17.
 */
public class CompactListElement<E> extends GridPane {
    private E element;

    private Region icon;
    private Label titleLabel;

    public CompactListElement(ListWidgetEntry<E> item) {
        super();

        this.element = item.getItem();

        this.getStyleClass().add("iconListCell");

        this.icon = new Region();
        this.icon.getStyleClass().add("iconListMiniatureImage");
        this.icon.setStyle(String.format("-fx-background-image: url(\"%s\");", item.getIconUri().toString()));

        this.titleLabel = new Label(item.getTitle());
        this.titleLabel.setWrapText(true);
        this.titleLabel.getStyleClass().add("information");

        List<ColumnConstraints> constraints = new ArrayList<>();
        constraints.add(new ColumnConstraints());
        constraints.add(new ColumnConstraintsWithPercentage(40));

        this.add(icon, 0, 0);
        this.add(titleLabel, 1, 0);

        item.getAdditionalInformation().ifPresent(additionInformations -> additionInformations.forEach(information -> {
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
        return element;
    }
}
