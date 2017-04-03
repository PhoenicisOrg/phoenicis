package org.phoenicis.javafx.views.common;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by marc on 01.04.17.
 */
public class ExpandedList<E, F> extends TransformationList<E, F> {
    private final Function<? super F, List<? extends E>> expander;

    private List<List<? extends E>> expandedValues;

    public ExpandedList(ObservableList<? extends F> source, Function<? super F, List<? extends E>> expander) {
        super(source);

        this.expander = expander;
        this.expandedValues = new ArrayList<List<? extends E>>();

        for (int index = 0; index < source.size(); index++) {
            List<? extends E> expanded = expander.apply(source.get(index));

            this.expandedValues.add(expanded);
        }
    }

    @Override
    public int getSourceIndex(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        int sum = 0;
        int sourceIndex = -1;

        for (int i = 0; i < getSource().size(); i++) {
            if (index < sum) {
                break;
            }

            sum += expandedValues.get(i).size();
            sourceIndex++;
        }

        return sourceIndex;
    }

    @Override
    public E get(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        E result = null;
        int start = 0;

        for (List<? extends E> values : expandedValues) {
            if (start + values.size() > index) {
                result = values.get(index - start);

                break;
            } else {
                start += values.size();
            }
        }

        return result;
    }

    @Override
    public int size() {
        return expandedValues.stream().mapToInt(List::size).sum();
    }

    private int getFirstPosition(int sourceIndex) {
        int position = 0;

        for (int i = 0; i < sourceIndex; i++) {
            position += expandedValues.get(i).size();
        }

        return position;
    }

    private int getLastPosition(int sourceIndex) {
        int position = 0;

        for (int i = 0; i <= sourceIndex; i++) {
            position += expandedValues.get(i).size();
        }

        return position;
    }

    private void permutate(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            int counter = 0;
            List<List<Integer>> perm = new ArrayList<>();
            for (List<? extends E> values : expandedValues) {
                perm.add(IntStream.range(counter, counter + values.size()).boxed().collect(Collectors.toList()));
                counter += values.size();
            }

            List<List<Integer>> permClone = new ArrayList<>(perm);
            List<List<? extends E>> valuesClone = new ArrayList<List<? extends E>>(expandedValues);

            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstPosition(i);
                int lastOldIndexPlusOne = getLastPosition(i);

                int firstNewIndex = getFirstPosition(c.getPermutation(i));

                int numberOfElements = lastOldIndexPlusOne - firstOldIndex;

                valuesClone.set(i, expandedValues.get(c.getPermutation(i)));
                permClone.set(i, IntStream.range(firstNewIndex, firstNewIndex + numberOfElements).boxed().collect(Collectors.toList()));
            }

            this.expandedValues = valuesClone;

            nextPermutation(from, to, permClone.stream().flatMap(List::stream).mapToInt(Integer::intValue).toArray());
        }
    }

    private void update(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstPosition(i);
                int lastOldIndexPlusOne = getLastPosition(i);

                List<? extends E> oldValues = expandedValues.get(i);

                List<? extends E> newValues = expander.apply(getSource().get(i));

                expandedValues.set(i, newValues);

                if (oldValues.size() > newValues.size()) {
                    for (int count = 0; count < newValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }

                    for (int count = newValues.size(); count < oldValues.size(); count++) {
                        nextRemove(firstOldIndex + count, oldValues.get(count));
                    }
                }

                if (oldValues.size() < newValues.size()) {
                    for (int count = 0; count < oldValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }

                    nextAdd(firstOldIndex + oldValues.size(), firstOldIndex + newValues.size());
                }

                if (oldValues.size() == newValues.size()) {
                    for (int count = 0; count < oldValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }
                }
            }
        }
    }

    private void addRemove(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            int firstOldIndex = getFirstPosition(index);

            nextRemove(firstOldIndex, expandedValues.remove(index));
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            int lastOldIndex = getLastPosition(index - 1);

            List<? extends E> newValues = expander.apply(getSource().get(index));

            expandedValues.add(index, newValues);

            nextAdd(lastOldIndex, lastOldIndex + newValues.size());
        }
    }

    @Override
    protected void sourceChanged(ListChangeListener.Change<? extends F> c) {
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
