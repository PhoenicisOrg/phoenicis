package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.MappedList;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by marc on 28.03.17.
 */
public class LeftToggleGroup<E> extends LeftGroup {
    private final ToggleGroup toggleGroup;
    private final Optional<ToggleButton> allButton;

    private final VBox toggleButtonBox;

    private final ObservableList<E> elements;
    private final ObservableList<? extends ToggleButton> mappedToggleButtons;

    public static <T> LeftToggleGroup<T> create(String name, Function<T, ? extends ToggleButton> converter) {
        return new LeftToggleGroup<T>(name, converter);
    }

    public static <T> LeftToggleGroup<T> create(String name, Supplier<ToggleButton> allButtonSupplier, Function<T, ? extends ToggleButton> converter) {
        return new LeftToggleGroup<T>(name, allButtonSupplier, converter);
    }

    private LeftToggleGroup(String name, Function<E, ? extends ToggleButton> converter) {
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

        this.allButton = Optional.empty();

        ObservableList<Node> target = this.getChildren();
        target.add(toggleButtonBox);

        Bindings.bindContent(this.toggleButtonBox.getChildren(), this.mappedToggleButtons);
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

        ToggleButton createdAllButton = allButtonSupplier.get();
        createdAllButton.setToggleGroup(toggleGroup);

        this.allButton = Optional.of(createdAllButton);

        ObservableList<Node> target = this.getChildren();
        target.add(createdAllButton);
        target.add(toggleButtonBox);

        Bindings.bindContent(this.toggleButtonBox.getChildren(), this.mappedToggleButtons);
    }

    public ObservableList<E> getElements() {
        return this.elements;
    }

    public void selectAll() {
        this.allButton.ifPresent(button -> button.setSelected(true));
    }

    public void select(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= this.elements.size()) {
            throw new IllegalArgumentException(String.format("Element at index %d doesn't exist", elementIndex));
        } else {
            mappedToggleButtons.get(elementIndex).fire();
        }
    }

    public void select(E element) {
        this.select(this.elements.indexOf(element));
    }
}
