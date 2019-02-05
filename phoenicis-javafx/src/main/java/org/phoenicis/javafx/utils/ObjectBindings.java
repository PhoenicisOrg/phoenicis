package org.phoenicis.javafx.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;

import java.util.Optional;
import java.util.function.Function;

/**
 * An utility class containing functions to more map {@link ObservableValue} objects to {@link ObservableValue} objects
 */
public class ObjectBindings {
    /**
     * Maps a {@link ObservableValue<I>} object to a {@link ObjectBinding<O>} by applying the given converter function.
     * In case the input value is empty, i.e. contains <code>null</code>, the given default value is used
     *
     * @param property     The input value
     * @param converter    The converter function
     * @param defaultValue The default value in case the input value is empty
     * @param <I>          The type of the input value
     * @param <O>          The type of the output value
     * @return A {@link ObjectBinding} containing the converted value
     */
    public static <I, O> ObjectBinding<O> map(ObservableValue<I> property, Function<I, O> converter, O defaultValue) {
        return Bindings.createObjectBinding(
                () -> Optional.ofNullable(property.getValue()).map(converter).orElse(defaultValue), property);
    }

    /**
     * Maps a {@link ObservableValue<I>} object to a {@link ObjectBinding<O>} by applying the given converter function
     *
     * @param property  The input value
     * @param converter The converter function
     * @param <I>       The type of the input value
     * @param <O>       The type of the output value
     * @return A {@link ObjectBinding} containing the converted value
     */
    public static <I, O> ObjectBinding<O> map(ObservableValue<I> property, Function<I, O> converter) {
        return map(property, converter, null);
    }
}
