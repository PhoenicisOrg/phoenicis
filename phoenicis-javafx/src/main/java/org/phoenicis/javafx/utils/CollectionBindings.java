package org.phoenicis.javafx.utils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
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

        final InvalidationListener listener = (Observable invalidation) -> {
            final I input = property.getValue();

            if (input != null) {
                result.setAll(converter.apply(input));
            } else {
                result.clear();
            }
        };

        // add the listener to the property
        property.addListener(listener);

        // ensure that the result list is initialised correctly
        listener.invalidated(property);

        return result;
    }
}
