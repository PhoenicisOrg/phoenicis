package org.phoenicis.javafx.utils;

import com.google.common.collect.ImmutableMap;
import javafx.beans.Observable;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder class for <code>switch</code> like {@link ObjectBinding}s
 *
 * @param <E> The type of the analysed {@link ObjectProperty}
 * @param <T> The target type
 */
public class SwitchBindingBuilder<E, T> {
    /**
     * A map containing all known cases
     */
    private final Map<E, ObjectProperty<T>> cases;

    /**
     * The analysed {@link ObjectProperty}
     */
    private ObjectProperty<E> value;

    /**
     * The <code>default</code> case
     */
    private ObjectProperty<T> defaultCase;

    /**
     * Constructor
     */
    public SwitchBindingBuilder() {
        super();

        this.cases = new HashMap<>();
    }

    /**
     * Constructor
     *
     * @param value The analysed value
     */
    public SwitchBindingBuilder(ObjectProperty<E> value) {
        this();

        this.value = value;
    }

    /**
     * Sets the analysed value to the given {@link ObjectProperty value}
     *
     * @param value The new analysed value
     * @return The used {@link SwitchBindingBuilder} instance
     */
    public SwitchBindingBuilder<E, T> withValue(ObjectProperty<E> value) {
        this.value = value;

        return this;
    }

    /**
     * Adds a new case for the switch binding.
     *
     * @param guard The guard/case
     * @param result The resulting value if the guard/case is fulfilled
     * @return The used {@link SwitchBindingBuilder} instance
     */
    public SwitchBindingBuilder<E, T> withCase(E guard, ObjectProperty<T> result) {
        this.cases.put(guard, result);

        return this;
    }

    /**
     * Adds a new case for the switch binding.
     *
     * @param guard The guard/case
     * @param result The resulting value if the guard/case is fulfilled
     * @return The used {@link SwitchBindingBuilder} instance
     */
    public SwitchBindingBuilder<E, T> withCase(E guard, T result) {
        this.cases.put(guard, new SimpleObjectProperty<>(result));

        return this;
    }

    /**
     * Sets the default case for the switch binding
     *
     * @param result The resulting value if no other guard/case is fulfilled
     * @return The used {@link SwitchBindingBuilder} instance
     */
    public SwitchBindingBuilder<E, T> withDefaultCase(ObjectProperty<T> result) {
        this.defaultCase = result;

        return this;
    }

    /**
     * Sets the default case for the switch binding to an empty result
     *
     * @return The used {@link SwitchBindingBuilder} instance
     */
    public SwitchBindingBuilder<E, T> withEmptyDefaultCase() {
        return withDefaultCase(new SimpleObjectProperty<>());
    }

    /**
     * Builds a new {@link ObjectBinding} reflecting the defined <code>switch</code> construct
     *
     * @return A new {@link ObjectBinding} reflecting the defined <code>switch</code> construct
     */
    public ObjectBinding<T> build() {
        final ObjectProperty<E> buildValue = value;
        final ObjectProperty<T> buildDefaultCase = defaultCase;
        final Map<E, ObjectProperty<T>> buildCases = ImmutableMap.copyOf(cases);

        return new ObjectBinding<T>() {
            {
                // bind to the value
                bind(buildValue);
                // bind to the cases
                for (Observable buildCase : buildCases.values()) {
                    bind(buildCase);
                }
            }

            @Override
            protected T computeValue() {
                final E current = buildValue.getValue();

                final ObjectProperty<T> result = buildDefaultCase != null
                        ? buildCases.getOrDefault(current, buildDefaultCase)
                        : buildCases.get(current);

                return result.getValue();
            }

            @Override
            public void dispose() {
                // unbind from the cases
                for (Observable buildCase : buildCases.values()) {
                    super.unbind(buildCase);
                }
                // unbind from the value
                super.unbind(buildValue);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(buildValue);
            }
        };
    }
}
