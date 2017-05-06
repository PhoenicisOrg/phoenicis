package org.phoenicis.javafx.views.common;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by marc on 01.04.17.
 */
public class MappedList<E, F> extends TransformationList<E, F> {
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

    private void permutate(Change<? extends F> c) {
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

    private void update(Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                mappedValues.set(i, mapper.apply(getSource().get(i)));

                nextUpdate(i);
            }
        }
    }

    private void addRemove(Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            nextRemove(index, mappedValues.remove(index));
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            mappedValues.add(index, mapper.apply(getSource().get(index)));

            nextAdd(index, index + 1);
        }
    }

    @Override
    protected void sourceChanged(Change<? extends F> c) {
        beginChange();
        while (c.next()) {
            if (c.wasPermutated()) {
                permutate(c);
            } else if (c.wasUpdated()) {
                update(c);
            } else {
                addRemove(c);
            }
        }
        endChange();
    }
}