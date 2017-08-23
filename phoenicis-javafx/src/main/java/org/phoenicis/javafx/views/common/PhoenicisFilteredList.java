package org.phoenicis.javafx.views.common;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PhoenicisFilteredList<E> extends TransformationList<E, E> {
    private List<Boolean> filtered;

    /**
     * The predicate that will match the elements that will be in this FilteredList.
     * Elements not matching the predicate will be filtered-out.
     * Null predicate means "always true" predicate, all elements will be matched.
     */
    private Predicate<? super E> predicate;

    /**
     * Constructs a new FilteredList wrapper around the source list.
     * The provided predicate will match the elements in the source list that will be visible.
     * If the predicate is null, all elements will be matched and the list is equal to the source list.
     *
     * @param source    the source list
     * @param predicate the predicate to match the elements or null to match all elements.
     */
    public PhoenicisFilteredList(ObservableList<E> source, Predicate<? super E> predicate) {
        super(source);

        this.filtered = source.stream().map(predicate::test).collect(Collectors.toList());
        this.predicate = predicate;
    }

    @Override
    protected void sourceChanged(ListChangeListener.Change<? extends E> c) {
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

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return this.filtered.stream().filter(value -> value).collect(Collectors.counting()).intValue();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public E get(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        int i = -1;
        int count = -1;
        while (count < index) {
            if (filtered.get(++i)) {
                count++;
            }
        }

        return getSource().get(i);
    }

    @Override
    public int getSourceIndex(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        int i = -1;
        int count = -1;
        while (count < index) {
            if (filtered.get(++i)) {
                count++;
            }
        }

        return i;
    }

    private int getIndexToSourceIndex(int sourceIndex) {
        return (int) this.filtered.subList(0, sourceIndex).stream().filter(value -> value).count();
    }

    private void permutate(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            List<Boolean> clone = new ArrayList<>(filtered);
            int[] perm = IntStream.range(0, size()).toArray();

            for (int i = from; i < to; ++i) {
                perm[getIndexToSourceIndex(i)] = getIndexToSourceIndex(c.getPermutation(i));

                filtered.set(i, clone.get(c.getPermutation(i)));
            }

            nextPermutation(from, to, perm);
        }
    }

    private void addRemove(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            int internalIndex = getIndexToSourceIndex(index);

            if (filtered.remove(index)) {
                nextRemove(internalIndex, getSource().get(index));
            }
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            filtered.add(index, predicate.test(getSource().get(index)));

            if (filtered.get(index)) {
                int internalIndex = getIndexToSourceIndex(index);

                nextAdd(internalIndex, internalIndex + 1);
            }
        }
    }

    private void update(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                boolean oldFiltered = filtered.get(i);

                filtered.set(i, predicate.test(getSource().get(i)));

                if (oldFiltered && filtered.get(i)) {
                    nextUpdate(getIndexToSourceIndex(i));
                } else if (oldFiltered && !filtered.get(i)) {
                    nextRemove(getIndexToSourceIndex(i), getSource().get(i));
                } else if (!oldFiltered && filtered.get(i)) {
                    int internalIndex = getIndexToSourceIndex(i);

                    nextAdd(internalIndex, internalIndex + 1);
                }
            }
        }
    }

    public void trigger() {
        beginChange();

        for (int index = getSource().size() - 1; index >= 0; index--) {
            /*
             * The element was contained before but not anymore
             */
            if (filtered.get(index) && !predicate.test(getSource().get(index))) {
                filtered.set(index, false);

                nextRemove(getIndexToSourceIndex(index), getSource().get(index));
            }
        }

        for (int index = 0; index < getSource().size(); index++) {
            /*
             * The element wasn't contained before, but is now
             */
            if (!filtered.get(index) && predicate.test(getSource().get(index))) {
                filtered.set(index, true);

                int internalIndex = getIndexToSourceIndex(index);

                nextAdd(internalIndex, internalIndex + 1);
            }
        }

        endChange();
    }
}
