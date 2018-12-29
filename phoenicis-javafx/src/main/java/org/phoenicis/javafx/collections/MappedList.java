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
 * Created by marc on 01.04.17.
 */
public class MappedList<E, F> extends PhoenicisTransformationList<E, F> {
    private final ObjectProperty<Function<? super F, ? extends E>> mapper;

    private final List<E> mappedValues;

    public MappedList(ObservableList<? extends F> source, ObjectProperty<Function<? super F, ? extends E>> mapper) {
        super(source);

        this.mapper = mapper;
        this.mappedValues = new ArrayList<>();

        Optional.ofNullable(mapper.getValue())
                .ifPresent(mapperFunction -> source.stream().map(mapperFunction).forEach(mappedValues::add));

        mapper.addListener((observable, oldMapper, newMapper) -> {
            beginChange();
            if (oldMapper != null && newMapper == null) {
                mappedValues.clear();

                nextRemove(0, mappedValues);
            }
            if (oldMapper == null && newMapper != null) {
                for (F element : getSource()) {
                    mappedValues.add(newMapper.apply(element));
                }

                nextAdd(0, size());
            }
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

        fireChange(new InitialisationChange<>(0, size(), this));
    }

    public MappedList(ObservableList<? extends F> source, Function<? super F, ? extends E> mapper) {
        this(source, new SimpleObjectProperty<>(mapper));
    }

    public MappedList(ObservableList<? extends F> source) {
        this(source, new SimpleObjectProperty<>());
    }

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

    @Override
    public E get(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return mappedValues.get(index);
    }

    @Override
    public int size() {
        return Optional.ofNullable(getMapper())
                .map(mapper -> getSource().size()).orElse(0);
    }

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