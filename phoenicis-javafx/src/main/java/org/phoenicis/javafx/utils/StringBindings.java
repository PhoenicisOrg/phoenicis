package org.phoenicis.javafx.utils;

import com.sun.javafx.binding.Logging;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public static <E> StringBinding flatMap(ObservableValue<E> property, Function<E, ObservableValue<String>> converter,
            String defaultValue) {
        return new StringBinding() {
            private ObservableValue<String> currentValue;

            {
                bind(property);
            }

            @Override
            protected String computeValue() {
                try {
                    if (currentValue != null) {
                        super.unbind(currentValue);

                        this.currentValue = null;
                    }

                    ObservableValue<String> value = Optional.ofNullable(property.getValue()).map(converter)
                            .orElse(null);

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

    public static <E> StringBinding flatMap(ObservableValue<E> property,
            Function<E, ObservableValue<String>> converter) {
        return flatMap(property, converter, null);
    }

    public static <E> StringProperty mutableMap(ObservableValue<E> property, Function<E, Property<String>> converter) {
        final StringProperty result = new SimpleStringProperty();

        final ObjectBinding<Property<String>> mapping = ObjectBindings.map(property, converter);

        mapping.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.unbindBidirectional(result);
            }

            if (newValue != null) {
                newValue.bindBidirectional(result);
            }
        });

        Optional.ofNullable(mapping.getValue()).ifPresent(newValue -> newValue.bindBidirectional(result));

        return result;
    }
}
