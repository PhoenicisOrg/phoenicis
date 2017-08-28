package org.phoenicis.javafx.views.common.lists;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A filtered observable list taking a {@link Predicate} to filter elements of a given input {@link ObservableList}.
 * This class differs from {@link javafx.collections.transformation.FilteredList} through the way it reacts on filter function changes.
 * {@link javafx.collections.transformation.FilteredList} marks the complete list as invalid and refilters it after the filter function has changes.
 * In comparison, this class only marks the elements as invalid, that were previously added and now aren't, or the other way.
 *
 * @param <E> The type of the elements contained in the filtered list
 * @see javafx.collections.transformation.FilteredList
 * @author Marc Arndt
 */
public class PhoenicisFilteredList<E> extends PhoenicisTransformationList<E, E> {
    /**
     * A list containing a boolean for each element inside {@link TransformationList#getSource()}, describing if the element should be filtered or not
     */
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
     * @param predicate the predicate to match the elements
     */
    public PhoenicisFilteredList(ObservableList<E> source, Predicate<? super E> predicate) {
        super(source);

        this.filtered = source.stream().map(predicate::test).collect(Collectors.toList());
        this.predicate = predicate;
    }

    /**
     * {@inheritDoc}
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
     * @throws IndexOutOfBoundsException
     */
    @Override
    public E get(int index) {
        return getSource().get(getSourceIndex(index));
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Takes an index value from the source list and converts it to the index in this filtered list.
     * This method will only return the correct result if the value at the source index position matches the filter predicate.
     *
     * @param sourceIndex The index in the source list
     * @return The index in this filtered list
     */
    private int getIndexToSourceIndex(int sourceIndex) {
        return (int) this.filtered.subList(0, sourceIndex).stream().filter(value -> value).count();
    }

    /**
     * Processes the permutation part inside the change object, i.e. two or more elements inside the source list are permutated.
     *
     * @param c The change object to process
     */
    protected void permute(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            List<Boolean> clone = new ArrayList<>(filtered);
            int[] perm = IntStream.range(0, size()).toArray();

            for (int i = from; i < to; ++i) {
                perm[getIndexToSourceIndex(i)] = getIndexToSourceIndex(c.getPermutation(i));

                clone.set(i, filtered.get(c.getPermutation(i)));
            }

            filtered = clone;

            nextPermutation(from, to, perm);
        }
    }

    /**
     * Processes the add and remove part inside the change object.
     *
     * @param c The change object to process
     */
    protected void addRemove(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        /*
         * Process all removals
         */
        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            int internalIndex = getIndexToSourceIndex(index);

            if (filtered.remove(index)) {
                nextRemove(internalIndex, c.getRemoved().get(index - from));
            }
        }

        /*
         * Process all additions
         */
        for (int index = from; index < from + c.getAddedSize(); index++) {
            filtered.add(index, predicate.test(getSource().get(index)));

            if (filtered.get(index)) {
                int internalIndex = getIndexToSourceIndex(index);

                nextAdd(internalIndex, internalIndex + 1);
            }
        }
    }

    /**
     * Processes the value updates inside the change object.
     *
     * @param c The change object to process
     */
    protected void update(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                boolean oldFiltered = filtered.get(i);

                filtered.set(i, predicate.test(getSource().get(i)));

                /*
                 * The value was previously contained inside the filtered list and the new value is still contained
                 */
                if (oldFiltered && filtered.get(i)) {
                    nextUpdate(getIndexToSourceIndex(i));
                }
                /*
                 * The value was previously contained inside the filtered list but the new value is not contained anymore
                 */
                else if (oldFiltered && !filtered.get(i)) {
                    nextRemove(getIndexToSourceIndex(i), getSource().get(i));
                }
                /*
                 * The value wasn't previously contained inside the filtered list but the new value is now contained
                 */
                else if (!oldFiltered && filtered.get(i)) {
                    int internalIndex = getIndexToSourceIndex(i);

                    nextAdd(internalIndex, internalIndex + 1);
                }
            }
        }
    }

    /**
     * Triggers a check of all elements inside the source list to check if they still match the predicate function.
     * Only if they are now contained inside this filtered list and were not contained previously,
     * or the other way around, will a {@link javafx.collections.ListChangeListener.Change} event be triggered for the element.
     */
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
