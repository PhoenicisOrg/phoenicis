package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by marc on 28.03.17.
 */
public class LeftToggleGroup<E> extends LeftGroup {
    private final ToggleButton allButton;

    private final ToggleGroup toggleGroup;

    private final ObservableList<E> elements;

    public static <T> LeftToggleGroup<T> create(String name, Supplier<ToggleButton> allButtonSupplier, Function<T, ? extends ToggleButton> converter) {
        return new LeftToggleGroup<T>(name, allButtonSupplier, converter);
    }

    private LeftToggleGroup(String name, Supplier<ToggleButton> allButtonSupplier, Function<E, ? extends ToggleButton> converter) {
        super(name);

        this.elements = FXCollections.observableArrayList();
        this.toggleGroup = new ToggleGroup();

        this.allButton = allButtonSupplier.get();
        this.allButton.setToggleGroup(toggleGroup);

        ObservableList<Node> target = this.getChildren();
        target.add(allButton);

        this.elements.addListener((ListChangeListener<? super E>) change -> {
            while (change.next()) {
                int from = change.getFrom();
                int to = change.getTo();
                if (change.wasPermutated()) {
                    target.subList(from + 2, to + 2).clear();
                    target.addAll(from + 2, elements.subList(from, to).stream().map(element -> {
                        ToggleButton result = converter.apply(element);

                        result.setToggleGroup(toggleGroup);

                        return result;
                    }).collect(Collectors.toList()));
                } else {
                    target.subList(from + 2, from + 2 + change.getRemovedSize()).clear();
                    target.addAll(from + 2, elements.subList(from, from + change.getAddedSize()).stream().map(element -> {
                        ToggleButton result = converter.apply(element);

                        result.setToggleGroup(toggleGroup);

                        return result;
                    }).collect(Collectors.toList()));
                }
            }
        });
    }

    public ObservableList<E> getElements() {
        return this.elements;
    }

    public void selectAll() {
        this.allButton.setSelected(true);
    }
}
