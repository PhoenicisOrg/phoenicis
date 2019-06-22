package org.phoenicis.javafx.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;

import java.util.Optional;
import java.util.function.Function;

/**
 * A utility class containing functions to map {@link ObservableValue} objects to {@link StringBinding} objects
 */
public final class StringBindings {
    /**
     * Maps a {@link ObservableValue} object to a {@link StringBinding} by applying the given converter function.
     * In case the input value is empty, i.e. contains <code>null</code>, the given default value is used
     *
     * @param property The input value
     * @param converter The converter function
     * @param defaultValue The default value in case the input value is empty
     * @param <E> The type of the input value
     * @return A {@link StringBinding} containing the converted value
     */
    public static <E> StringBinding map(ObservableValue<E> property, Function<E, String> converter,
            String defaultValue) {
        return Bindings.createStringBinding(
                () -> Optional.ofNullable(property.getValue()).map(converter).orElse(defaultValue), property);
    }

    /**
     * Maps a {@link ObservableValue} object to a {@link StringBinding} by applying the given converter function
     *
     * @param property The input value
     * @param converter The converter function
     * @param <E> The type of the input value
     * @return A {@link StringBinding} containing the converted value
     */
    public static <E> StringBinding map(ObservableValue<E> property, Function<E, String> converter) {
        return map(property, converter, null);
    }
}
