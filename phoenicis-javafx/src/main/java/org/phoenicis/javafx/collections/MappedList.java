package org.phoenicis.javafx.collections;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.collections.change.InitialisationChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * An implementation of a mapped {@link ObservableList}, which maps the values of the source {@link ObservableList} into
 * values of the target type {@link E}.
 *
 * @param <E> The instance type of the target elements
 * @param <F> The instance type of the source elements
 */
public class MappedList<E, F> extends PhoenicisTransformationList<E, F> {
    /**
     * The mapper function used to map the source values to the target type {@link E}.
     * If the mapper function is set to <code>null</code> the list acts as if it were empty
     */
    private final ObjectProperty<Function<? super F, ? extends E>> mapper;

    /**
     * A list of all mapped values
     */
    private final List<E> mappedValues;

    /**
     * Constructor
     *
     * @param source The source list
     * @param mapper The mapper function
     */
    public MappedList(ObservableList<? extends F> source, ObjectProperty<Function<? super F, ? extends E>> mapper) {
        super(source);

        this.mapper = mapper;
        this.mappedValues = new ArrayList<>();

        // create a cache of all mapped source elements
        Optional.ofNullable(getMapper())
                .ifPresent(mapperFunction -> source.stream().map(mapperFunction).forEach(mappedValues::add));

        // add a listener to detect changes of the mapper function
        mapper.addListener((observable, oldMapper, newMapper) -> {
            beginChange();
            // the mapper function has been set to null
            if (oldMapper != null && newMapper == null) {
                mappedValues.clear();

                nextRemove(0, mappedValues);
            }

            // the mapper function has been set from null to another value
            if (oldMapper == null && newMapper != null) {
                for (F element : getSource()) {
                    mappedValues.add(newMapper.apply(element));
                }

                nextAdd(0, size());
            }

            // the mapper function has been changed (from not null to not null)
            if (oldMapper != null && newMapper != null) {
                mappedValues.clear();

                for (int index = 0; index < size(); index++) {
                    final F element = getSource().get(index);

                    mappedValues.add(newMapper.apply(element));

                    nextUpdate(index);
                }
            }
            endChange();
        });

        // fire an initialisation event containing all mapped elements
        fireChange(new InitialisationChange<>(0, size(), this));
    }

    /**
     * Constructor
     *
     * @param source The source list
     * @param mapper The mapper function
     */
    public MappedList(ObservableList<? extends F> source, Function<? super F, ? extends E> mapper) {
        this(source, new SimpleObjectProperty<>(mapper));
    }

    /**
     * Constructor
     *
     * @param source The source list
     */
    public MappedList(ObservableList<? extends F> source) {
        this(source, new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceIndex(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return index;
    }

    /**
     * Finds the index of the first element in the source list at the given index position
     *
     * @param index The index in the source list
     * @return The index of the first element of the source index in this list
     * @apiNote This method is required to make Phoenicis compile with Java 9
     */
    public int getViewIndex(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return mappedValues.get(index);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If no mapper function is set, the size of this list is always <code>0</code>, otherwise it equals
     * the size of the source list
     */
    @Override
    public int size() {
        return Optional.ofNullable(getMapper())
                .map(mapper -> getSource().size()).orElse(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void permute(Change<? extends F> c) {
        final int from = c.getFrom();
        final int to = c.getTo();

        if (to > from) {
            final List<E> clone = new ArrayList<>(mappedValues);
            final int[] perm = IntStream.range(0, size()).toArray();

            for (int i = from; i < to; ++i) {
                perm[i] = c.getPermutation(i);
                mappedValues.set(i, clone.get(c.getPermutation(i)));
            }

            nextPermutation(from, to, perm);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void update(Change<? extends F> c) {
        final int from = c.getFrom();
        final int to = c.getTo();

        final Function<? super F, ? extends E> mapper = getMapper();

        if (mapper != null) {
            for (int i = from; i < to; ++i) {
                mappedValues.set(i, mapper.apply(getSource().get(i)));

                nextUpdate(i);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addRemove(Change<? extends F> c) {
        final int from = c.getFrom();

        final Function<? super F, ? extends E> mapper = getMapper();

        if (mapper != null) {
            for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
                nextRemove(index, mappedValues.remove(index));
            }

            for (int index = from; index < from + c.getAddedSize(); index++) {
                mappedValues.add(index, mapper.apply(getSource().get(index)));

                nextAdd(index, index + 1);
            }
        }
    }

    public Function<? super F, ? extends E> getMapper() {
        return mapper.get();
    }

    public ObjectProperty<Function<? super F, ? extends E>> mapperProperty() {
        return mapper;
    }

    public void setMapper(Function<? super F, ? extends E> mapper) {
        this.mapper.set(mapper);
    }
}
