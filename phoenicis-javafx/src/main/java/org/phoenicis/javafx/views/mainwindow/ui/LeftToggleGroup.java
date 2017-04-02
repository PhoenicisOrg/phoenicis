package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.MappedList;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by marc on 28.03.17.
 */
public class LeftToggleGroup<E> extends LeftGroup {
    private final ToggleGroup toggleGroup;
    private final ToggleButton allButton;

    private final VBox toggleButtonBox;

    private final ObservableList<E> elements;
    private final ObservableList<? extends ToggleButton> mappedToggleButtons;

    public static <T> LeftToggleGroup<T> create(String name, Supplier<ToggleButton> allButtonSupplier, Function<T, ? extends ToggleButton> converter) {
        return new LeftToggleGroup<T>(name, allButtonSupplier, converter);
    }

    private LeftToggleGroup(String name, Supplier<ToggleButton> allButtonSupplier, Function<E, ? extends ToggleButton> converter) {
        super(name);

        this.toggleGroup = new ToggleGroup();
        this.toggleButtonBox = new VBox();
        this.toggleButtonBox.getStyleClass().add("leftPaneInside");

        this.elements = FXCollections.observableArrayList();
        this.mappedToggleButtons = new MappedList<ToggleButton, E>(this.elements, value -> {
            ToggleButton button = converter.apply(value);

            button.setToggleGroup(toggleGroup);

            return button;
        });

        this.allButton = allButtonSupplier.get();
        this.allButton.setToggleGroup(toggleGroup);

        ObservableList<Node> target = this.getChildren();
        target.add(allButton);
        target.add(toggleButtonBox);

        Bindings.bindContent(this.toggleButtonBox.getChildren(), this.mappedToggleButtons);
    }

    public ObservableList<E> getElements() {
        return this.elements;
    }

    public void selectAll() {
        this.allButton.setSelected(true);
    }
}
