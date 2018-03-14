package org.phoenicis.javafx.views.common.lists;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by marc on 01.04.17.
 */
public class MappedList<E, F> extends PhoenicisTransformationList<E, F> {
    private final Function<? super F, ? extends E> mapper;

    private List<E> mappedValues;

    public MappedList(ObservableList<? extends F> source, Function<? super F, ? extends E> mapper) {
        super(source);

        this.mappedValues = source.stream().map(mapper).collect(Collectors.toList());
        this.mapper = mapper;
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
        return mappedValues.size();
    }

    protected void permute(Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            List<E> clone = new ArrayList<E>(mappedValues);
            int[] perm = IntStream.range(0, size()).toArray();

            for (int i = from; i < to; ++i) {
                perm[i] = c.getPermutation(i);
                mappedValues.set(i, clone.get(c.getPermutation(i)));
            }

            nextPermutation(from, to, perm);
        }
    }

    protected void update(Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                mappedValues.set(i, mapper.apply(getSource().get(i)));

                nextUpdate(i);
            }
        }
    }

    protected void addRemove(Change<? extends F> c) {
        int from = c.getFrom();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            nextRemove(index, mappedValues.remove(index));
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            mappedValues.add(index, mapper.apply(getSource().get(index)));

            nextAdd(index, index + 1);
        }
    }
}