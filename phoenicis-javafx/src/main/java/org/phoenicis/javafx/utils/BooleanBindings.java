package org.phoenicis.javafx.utils;

import com.sun.javafx.binding.Logging;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.Function;

public class BooleanBindings {
    /**
     * Maps a {@link ObservableValue} object to a {@link BooleanBinding} by applying the given converter function.
     * In case the input value is empty, i.e. contains <code>null</code>, the given default value is used
     *
     * @param property The input value
     * @param converter The converter function
     * @param defaultValue The default value in case the input value is empty
     * @param <E> The type of the input value
     * @return A {@link BooleanBinding} containing the converted value
     */
    public static <E> BooleanBinding map(ObservableValue<E> property, Function<E, Boolean> converter,
            boolean defaultValue) {
        return Bindings.createBooleanBinding(
                () -> Optional.ofNullable(property.getValue()).map(converter).orElse(defaultValue), property);
    }

    /**
     * Maps a {@link ObservableValue} object to a {@link BooleanBinding} by applying the given converter function
     *
     * @param property The input value
     * @param converter The converter function
     * @param <E> The type of the input value
     * @return A {@link BooleanBinding} containing the converted value
     */
    public static <E> BooleanBinding map(ObservableValue<E> property, Function<E, Boolean> converter) {
        return map(property, converter, false);
    }

    public static <E> BooleanBinding flatMap(ObservableValue<E> property,
            Function<E, ObservableValue<Boolean>> converter, boolean defaultValue) {
        return new BooleanBinding() {
            private ObservableValue<Boolean> currentValue;

            {
                bind(property);
            }

            @Override
            protected boolean computeValue() {
                try {
                    if (currentValue != null) {
                        super.unbind(currentValue);

                        this.currentValue = null;
                    }

                    ObservableValue<Boolean> value = Optional.ofNullable(property.getValue()).map(converter)
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

    public static <E> BooleanBinding flatMap(ObservableValue<E> property,
            Function<E, ObservableValue<Boolean>> converter) {
        return flatMap(property, converter, false);
    }

    public static <E> BooleanProperty mutableMap(ObservableValue<E> property,
            Function<E, Property<Boolean>> converter) {
        final BooleanProperty result = new SimpleBooleanProperty();

        final ObjectBinding<Property<Boolean>> mapping = ObjectBindings.map(property, converter);

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
