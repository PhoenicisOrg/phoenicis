package org.phoenicis.javafx.utils;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

/**
 * A utility class containing functions to map {@link ObservableValue} objects to {@link ObservableList} objects
 */
public class CollectionBindings {
    /**
     * Maps a {@link ObservableValue<I>} object to a {@link ObservableList<O>} by applying the given converter function
     * and adding the result to the {@link ObservableList<O>}.
     * In case the input value is empty, i.e. contains <code>null</code>, the returned {@link ObservableList<O>} is
     * empty
     *
     * @param property The input value
     * @param converter The converter function
     * @param <I> The type of the input value
     * @param <O> The type of the output value
     * @return A {@link ObservableList} containing the converted list
     */
    public static <I, O> ObservableList<O> mapToList(ObservableValue<I> property,
            Function<I, ? extends Collection<O>> converter) {
        final ObservableList<O> result = FXCollections.observableArrayList();

        property.addListener((Observable invalidation) -> {
            final I input = property.getValue();

            if (input != null) {
                result.setAll(converter.apply(input));
            } else {
                result.clear();
            }
        });

        return result;
    }

    public static <I, O> ObservableList<O> mapToObservableList(ObservableValue<I> property,
            Function<I, ? extends ObservableList<O>> converter) {
        final ObservableList<O> result = FXCollections.observableArrayList();

        final ObjectBinding<? extends ObservableList<O>> mapping = ObjectBindings.map(property, converter);

        mapping.addListener((observable, oldList, newList) -> {
            if (oldList != null) {
                Bindings.unbindContent(result, oldList);
            }

            if (newList != null) {
                Bindings.bindContent(result, newList);
            } else {
                result.clear();
            }
        });

        Optional.ofNullable(mapping.getValue()).ifPresent(newList -> Bindings.bindContent(result, newList));

        return result;
    }
}
