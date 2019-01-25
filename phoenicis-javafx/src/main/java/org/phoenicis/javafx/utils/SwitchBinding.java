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
 * A <code>switch</code> like {@link ObjectBinding}
 *
 * @param <V> The type of the analysed {@link ObjectProperty}
 * @param <C> The target type
 */
public class SwitchBinding<V, C> extends ObjectBinding<C> {
    /**
     * The analysed {@link ObjectProperty}
     */
    private final ObjectProperty<V> value;

    /**
     * The <code>default</code> case
     */
    private final ObjectProperty<C> defaultCase;

    /**
     * A map containing all known cases
     */
    private final Map<V, ObjectProperty<C>> cases;

    /**
     * Constructor
     *
     * @param value The analysed {@link ObjectProperty}
     * @param defaultCase The <code>default</code> case
     * @param cases A map containing all known cases
     */
    private SwitchBinding(ObjectProperty<V> value, ObjectProperty<C> defaultCase, Map<V, ObjectProperty<C>> cases) {
        super();

        this.value = value;
        this.defaultCase = defaultCase;
        this.cases = cases;

        initialize();
    }

    /**
     * Constructs a new {@link SwitchBindingBuilder} instance without a set analysed value
     *
     * @param <V> The type of the analysed {@link ObjectProperty}
     * @param <C> The target type
     * @return The created {@link SwitchBindingBuilder} instance
     */
    public static <V, C> SwitchBindingBuilder<V, C> builder() {
        return new SwitchBindingBuilder<>();
    }

    /**
     * Constructs a new {@link SwitchBindingBuilder} instance with a set analysed value
     *
     * @param value The analysed {@link ObjectProperty}
     * @param <V> The type of the analysed {@link ObjectProperty}
     * @param <C> The target type
     * @return The created {@link SwitchBindingBuilder} instance
     */
    public static <V, C> SwitchBindingBuilder<V, C> builder(ObjectProperty<V> value) {
        return new SwitchBindingBuilder<>(value);
    }

    /**
     * Initializes the bindings
     */
    private void initialize() {
        // bind to the value
        bind(value);

        // bind to the cases
        for (Observable currentCase : cases.values()) {
            bind(currentCase);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected C computeValue() {
        final V current = value.getValue();

        final ObjectProperty<C> result = defaultCase != null
                ? cases.getOrDefault(current, defaultCase)
                : cases.get(current);

        return result.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        // unbind from the cases
        for (Observable currentCase : cases.values()) {
            super.unbind(currentCase);
        }

        // unbind from the value
        super.unbind(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservableList<?> getDependencies() {
        return FXCollections.singletonObservableList(value);
    }

    /**
     * A builder class for <code>switch</code> like {@link ObjectBinding}s
     * <p>
     * Example:
     *
     * <pre>
     * {@code
     * ObjectProperty<SomeEnum> value = new SimpleObjectProperty<>();
     *
     * ObjectBinding<X> binding = SwitchBinding.<SomeEnum, X>builder(value)
     *    .withCase(SomeEnum.A, a)
     *    .withCase(SomeEnum.B, b)
     *    .withCase(SomeEnum.C, c)
     *    .withDefaultCase(default)
     *    .build();
     * }
     * </pre>
     *
     * @param <V> The type of the analysed {@link ObjectProperty}
     * @param <C> The target type
     */
    public static class SwitchBindingBuilder<V, C> {
        /**
         * A map containing all known cases
         */
        private final Map<V, ObjectProperty<C>> cases;

        /**
         * The analysed {@link ObjectProperty}
         */
        private ObjectProperty<V> value;

        /**
         * The <code>default</code> case
         */
        private ObjectProperty<C> defaultCase;

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
        public SwitchBindingBuilder(ObjectProperty<V> value) {
            this();

            this.value = value;
        }

        /**
         * Sets the analysed value to the given {@link ObjectProperty value}
         *
         * @param value The new analysed value
         * @return The current {@link SwitchBindingBuilder} instance
         */
        public SwitchBindingBuilder<V, C> withValue(ObjectProperty<V> value) {
            this.value = value;

            return this;
        }

        /**
         * Adds a new case for the switch binding.
         *
         * @param guard The guard/case
         * @param result The resulting value if the guard/case is fulfilled
         * @return The current {@link SwitchBindingBuilder} instance
         */
        public SwitchBindingBuilder<V, C> withCase(V guard, ObjectProperty<C> result) {
            this.cases.put(guard, result);

            return this;
        }

        /**
         * Adds a new case for the switch binding.
         *
         * @param guard The guard/case
         * @param result The resulting value if the guard/case is fulfilled
         * @return The current {@link SwitchBindingBuilder} instance
         */
        public SwitchBindingBuilder<V, C> withCase(V guard, C result) {
            this.cases.put(guard, new SimpleObjectProperty<>(result));

            return this;
        }

        /**
         * Sets the default case for the switch binding
         *
         * @param result The resulting value if no other guard/case is fulfilled
         * @return The current {@link SwitchBindingBuilder} instance
         */
        public SwitchBindingBuilder<V, C> withDefaultCase(ObjectProperty<C> result) {
            this.defaultCase = result;

            return this;
        }

        /**
         * Sets the default case for the switch binding to an empty result
         *
         * @return The current {@link SwitchBindingBuilder} instance
         */
        public SwitchBindingBuilder<V, C> withEmptyDefaultCase() {
            return withDefaultCase(new SimpleObjectProperty<>());
        }

        /**
         * Builds a new {@link ObjectBinding} reflecting the defined <code>switch</code> construct
         *
         * @return A new {@link ObjectBinding} reflecting the defined <code>switch</code> construct
         */
        public SwitchBinding<V, C> build() {
            return new SwitchBinding<>(value, defaultCase, ImmutableMap.copyOf(cases));
        }
    }
}
