package org.phoenicis.javafx.utils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * A utility class containing functions to map {@link ObservableValue} objects to {@link ObservableList} objects
 */
public class CollectionBindings {
    /**
     * Maps an {@link ObservableValue<I>} object to an {@link ObservableList} by applying the given converter function
     * and adding the result to the {@link ObservableList}.
     * In case the input value is empty, i.e. contains <code>null</code>, the returned {@link ObservableList} is also
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

    /**
     * Maps an {@link ObservableValue<I>} object to an {@link ObservableMap} by applying the given converter function
     * and adding the result to the {@link ObservableMap}.
     * In case the input value is empty, i.e. contains <code>null</code>, the returned {@link ObservableMap} is also
     * empty
     *
     * @param property The input value
     * @param converter The converter function
     * @param <I> The type of the input value
     * @param <O1> The input type of the result map
     * @param <O2> The output type of the result map
     * @return A {@link ObservableMap} containing the converted map
     */
    public static <I, O1, O2> ObservableMap<O1, O2> mapToMap(ObservableValue<I> property,
            Function<I, Map<O1, O2>> converter) {
        final ObservableMap<O1, O2> result = FXCollections.observableHashMap();

        final InvalidationListener listener = (Observable invalidation) -> {
            final I input = property.getValue();

            result.clear();

            if (input != null) {
                result.putAll(converter.apply(input));
            }
        };

        // add the listener to the property
        property.addListener(listener);

        // ensure that the result map is initialised correctly
        listener.invalidated(property);

        return result;
    }
}
