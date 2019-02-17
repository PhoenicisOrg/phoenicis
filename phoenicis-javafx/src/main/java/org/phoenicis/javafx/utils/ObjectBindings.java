package org.phoenicis.javafx.utils;

import com.sun.javafx.binding.Logging;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.Function;

/**
 * A utility class containing functions to map {@link ObservableValue} objects to {@link ObservableValue} objects
 */
public class ObjectBindings {
    /**
     * Maps a {@link ObservableValue<I>} object to a {@link ObjectBinding<O>} by applying the given converter function.
     * In case the input value is empty, i.e. contains <code>null</code>, the given default value is used
     *
     * @param property The input value
     * @param converter The converter function
     * @param defaultValue The default value in case the input value is empty
     * @param <I> The type of the input value
     * @param <O> The type of the output value
     * @return A {@link ObjectBinding} containing the converted value
     */
    public static <I, O> ObjectBinding<O> map(ObservableValue<I> property, Function<I, O> converter, O defaultValue) {
        return Bindings.createObjectBinding(
                () -> Optional.ofNullable(property.getValue()).map(converter).orElse(defaultValue), property);
    }

    /**
     * Maps a {@link ObservableValue<I>} object to a {@link ObjectBinding<O>} by applying the given converter function
     *
     * @param property The input value
     * @param converter The converter function
     * @param <I> The type of the input value
     * @param <O> The type of the output value
     * @return A {@link ObjectBinding} containing the converted value
     */
    public static <I, O> ObjectBinding<O> map(ObservableValue<I> property, Function<I, O> converter) {
        return map(property, converter, null);
    }

    public static <I, O> ObjectBinding<O> flatMap(ObservableValue<I> property,
            Function<I, ObservableValue<O>> converter, O defaultValue) {
        return new ObjectBinding<O>() {
            private ObservableValue<O> currentValue;

            {
                bind(property);
            }

            @Override
            protected O computeValue() {
                try {
                    if (currentValue != null) {
                        super.unbind(currentValue);

                        this.currentValue = null;
                    }

                    ObservableValue<O> value = Optional.ofNullable(property.getValue()).map(converter).orElse(null);

                    if (value != null) {
                        this.currentValue = value;

                        bind(currentValue);
                    }

                    return Optional.ofNullable(value).map(ObservableValue::getValue).orElse(defaultValue);
                } catch (Exception e) {
                    Logging.getLogger().warning("Exception while evaluating binding", e);

                    return defaultValue;
                }
            }

            @Override
            public void dispose() {
                if (currentValue != null) {
                    super.unbind(currentValue);
                }

                super.unbind(property);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return currentValue == null ? FXCollections.singletonObservableList(property)
                        : new ImmutableObservableList<Observable>(property, currentValue);
            }
        };
    }

    public static <I, O> ObjectBinding<O> flatMap(ObservableValue<I> property,
            Function<I, ObservableValue<O>> converter) {
        return flatMap(property, converter, null);
    }

    public static <I, O> ObjectProperty<O> mutableMap(ObservableValue<I> property, Function<I, Property<O>> converter) {
        final ObjectProperty<O> result = new SimpleObjectProperty<>();

        final ObjectBinding<Property<O>> mapping = ObjectBindings.map(property, converter);

        mapping.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                result.unbindBidirectional(oldValue);
            }

            if (newValue != null) {
                result.bindBidirectional(newValue);
            }
        });

        Optional.ofNullable(mapping.getValue()).ifPresent(result::bindBidirectional);

        return result;
    }
}
