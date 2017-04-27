package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.AdhocList;
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
    private final ObservableList<? extends ToggleButton> adhocToggleButtons;

    public static <T> LeftToggleGroup<T> create(String name, Function<T, ? extends ToggleButton> converter) {
        return new LeftToggleGroup<T>(name, converter);
    }

    public static <T> LeftToggleGroup<T> create(String name, Supplier<ToggleButton> allButtonSupplier, Function<T, ? extends ToggleButton> converter) {
        return new LeftToggleGroup<T>(name, allButtonSupplier, converter);
    }

    private LeftToggleGroup(String name, Supplier<ToggleButton> allButtonSupplier, Function<E, ? extends ToggleButton> converter) {
        super(name);

        this.toggleGroup = new ToggleGroup();
        this.toggleButtonBox = new VBox();
        this.toggleButtonBox.getStyleClass().add("leftPaneInside");

        this.elements = FXCollections.observableArrayList();
        this.mappedToggleButtons = new MappedList<ToggleButton, E>(this.elements, value -> converter.apply(value));

        if (allButtonSupplier != null) {
            ToggleButton newAllButton = allButtonSupplier.get();

            this.allButton = Optional.of(newAllButton);
            this.adhocToggleButtons = new AdhocList<ToggleButton>(this.mappedToggleButtons, newAllButton);
        } else {
            this.allButton = Optional.empty();
            this.adhocToggleButtons = new AdhocList<ToggleButton>(this.mappedToggleButtons);
        }

        Bindings.bindContent(this.toggleGroup.getToggles(), this.adhocToggleButtons);
        Bindings.bindContent(this.toggleButtonBox.getChildren(), this.adhocToggleButtons);

        this.getChildren().add(this.toggleButtonBox);
    }

    private LeftToggleGroup(String name, Function<E, ? extends ToggleButton> converter) {
        this(name, null, converter);
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
