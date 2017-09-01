package org.phoenicis.javafx.views.common.lists;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by marc on 01.04.17.
 */
public class ExpandedList<E, F> extends PhoenicisTransformationList<E, F> {
    private final Function<? super F, List<? extends E>> expander;

    private List<List<? extends E>> expandedValues;

    public ExpandedList(ObservableList<? extends F> source, Function<? super F, List<? extends E>> expander) {
        super(source);

        this.expander = expander;
        this.expandedValues = source.stream().map(expander).collect(Collectors.toList());
    }

    @Override
    public int getSourceIndex(int index) {
        if (index < 0 || index >= size()) {
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
        if (index < 0 || index >= size()) {
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

    private int getFirstIndex(int sourceIndex) {
        int position = 0;

        for (int i = 0; i < sourceIndex; i++) {
            position += expandedValues.get(i).size();
        }

        return position;
    }

    private int getLastIndexPlusOne(int sourceIndex) {
        int position = 0;

        for (int i = 0; i <= sourceIndex; i++) {
            position += expandedValues.get(i).size();
        }

        return position;
    }

    protected void permute(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        int expandedFrom = getFirstIndex(from);
        int expandedTo = getLastIndexPlusOne(to - 1);

        if (to > from) {
            List<? extends E> beforePermutation = expandedValues.stream().flatMap(List::stream)
                    .collect(Collectors.toList());
            List<List<? extends E>> valuesClone = new ArrayList<List<? extends E>>(expandedValues);

            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstIndex(i);
                int lastOldIndexPlusOne = getLastIndexPlusOne(i);

                int firstNewIndex = getFirstIndex(c.getPermutation(i));

                int numberOfElements = lastOldIndexPlusOne - firstOldIndex;

                valuesClone.set(i, expandedValues.get(c.getPermutation(i)));
            }

            this.expandedValues = valuesClone;

            List<? extends E> afterPermutation = expandedValues.stream().flatMap(List::stream)
                    .collect(Collectors.toList());

            int[] perm = beforePermutation.stream().mapToInt(afterPermutation::indexOf).toArray();

            nextPermutation(expandedFrom, expandedTo, perm);
        }
    }

    protected void update(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstIndex(i);
                int lastOldIndexPlusOne = getLastIndexPlusOne(i);

                List<? extends E> oldValues = expandedValues.get(i);

                List<? extends E> newValues = expander.apply(getSource().get(i));

                expandedValues.set(i, newValues);

                if (oldValues.size() > newValues.size()) {
                    for (int count = 0; count < newValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }

                    nextRemove(firstOldIndex, oldValues.subList(newValues.size(), oldValues.size()));
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

    protected void addRemove(ListChangeListener.Change<? extends F> c) {
        int from = c.getFrom();
        int to = c.getTo();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            int firstOldIndex = getFirstIndex(index);

            nextRemove(firstOldIndex, expandedValues.remove(index));
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            int lastOldIndex = getLastIndexPlusOne(index - 1);

            List<? extends E> newValues = expander.apply(getSource().get(index));

            expandedValues.add(index, newValues);

            nextAdd(lastOldIndex, lastOldIndex + newValues.size());
        }
    }
}
