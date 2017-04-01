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

    private List<Integer> sourceMapping;
    private List<E> expandedValues;

    public ExpandedList(ObservableList<? extends F> source, Function<? super F, List<? extends E>> expander) {
        super(source);

        this.expander = expander;
        this.sourceMapping = new ArrayList<Integer>();
        this.expandedValues = new ArrayList<E>();

        for (int index = 0; index < source.size(); index++) {
            List<? extends E> expanded = expander.apply(source.get(index));

            this.sourceMapping.add(expanded.size());
            this.expandedValues.addAll(expanded);
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

            sum += sourceMapping.get(i);
            sourceIndex++;
        }

        return sourceIndex;
    }

    @Override
    public E get(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return expandedValues.get(index);
    }

    @Override
    public int size() {
        return expandedValues.size();
    }

    private int getFirstPosition(int sourceIndex) {
        int position = 0;

        for (int i = 0; i < sourceIndex; i++) {
            position += sourceMapping.get(i);
        }

        return position;
    }

    private int getLastPosition(int sourceIndex) {
        int position = 0;

        for (int i = 0; i <= sourceIndex; i++) {
            position += sourceMapping.get(i);
        }

        return position;
    }

    private void permutate(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            List<E> valuesClone = new ArrayList<E>(expandedValues);
            List<Integer> positionClone = new ArrayList<Integer>(sourceMapping);

            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstPosition(i);
                int lastOldIndexPlusOne = getLastPosition(i);

                int firstNewIndex = getFirstPosition(c.getPermutation(i));

                List<E> permutatedValues = new ArrayList<E>(valuesClone.subList(firstOldIndex, lastOldIndexPlusOne));

                expandedValues.removeAll(permutatedValues);

                if (firstNewIndex >= lastOldIndexPlusOne) {
                    expandedValues.addAll(firstNewIndex - (lastOldIndexPlusOne - 1 - firstOldIndex), permutatedValues);
                } else {
                    expandedValues.addAll(firstNewIndex, permutatedValues);
                }

                Collections.swap(positionClone, i, c.getPermutation(i));
            }

            this.sourceMapping = positionClone;

            int[] perm = sourceMapping.stream().flatMap(value -> IntStream.range(0, value).boxed()).mapToInt(i -> i).toArray();

            nextPermutation(from, to, perm);
        }
    }

    private void update(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstPosition(i);
                int lastOldIndexPlusOne = getLastPosition(i);

                // delete old values
                removeSourceValue(i, firstOldIndex, lastOldIndexPlusOne);

                // add new values
                addSourceValue(i, firstOldIndex);
            }
        }
    }

    /**
     * Processes the removal of an element in the source list.
     * This leads to the removal of all elements that were expanded from the source element in this list
     * @param index             The index of the removed element in the source list
     * @param firstIndex     The occurance of the first element, that was expanded from the to be deleted source element in this list
     * @param lastIndexPlusOne      The occurance of the last element, that was expanded from the to be deleted source element in this list
     */
    private void removeSourceValue(int index, int firstIndex, int lastIndexPlusOne) {
        for (int innerIndex = lastIndexPlusOne - 1; innerIndex >= firstIndex; innerIndex--) {
            nextRemove(innerIndex, expandedValues.remove(innerIndex));
        }

        sourceMapping.remove(index);
    }

    /**
     * Processes the addition of a new element in the source list.
     * This leads to the addition of all elements that can be expanded from the source element in this list at the given @param firstIndex
     * @param index             The index of the added element in the source list
     * @param firstIndex     The starting index of the first element, that was expanded from the new source value in this list
     */
    private void addSourceValue(int index, int firstIndex) {
        List<? extends E> newValues = expander.apply(getSource().get(index));

        sourceMapping.add(index, newValues.size());
        expandedValues.addAll(firstIndex, newValues);

        nextAdd(firstIndex, firstIndex + newValues.size());
    }

    private void addRemove(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            int firstOldIndex = getFirstPosition(index);
            int lastOldIndexPlusOne = getLastPosition(index);

            removeSourceValue(index, firstOldIndex, lastOldIndexPlusOne);
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            int lastOldIndex = getLastPosition(index - 1);

            addSourceValue(index, lastOldIndex);
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
