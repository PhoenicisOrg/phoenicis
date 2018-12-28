package org.phoenicis.javafx.collections;

import com.google.common.collect.ImmutableList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.collections.change.InitialisationChange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConcatenatedList<E> extends PhoenicisTransformationList<E, ObservableList<E>> {
    private final List<List<E>> expandedValues;

    public ConcatenatedList(ObservableList<? extends ObservableList<E>> source) {
        super(source);

        this.expandedValues = source.stream().map(ArrayList::new).collect(Collectors.toList());

        source.forEach(this::addUpdateListener);
        fireChange(new InitialisationChange<>(0, size(), this));
    }

    @SafeVarargs
    public static <F> ConcatenatedList<F> createPrefixList(ObservableList<F> list, F... prefixes) {
        return new ConcatenatedList<F>(FXCollections.observableArrayList(
                ImmutableList.<ObservableList<F>> builder()
                        .add(FXCollections.observableArrayList(prefixes))
                        .add(list).build()));
    }

    @SafeVarargs
    public static <F> ConcatenatedList<F> createSuffixList(ObservableList<F> list, F... suffixes) {
        return new ConcatenatedList<F>(FXCollections.observableArrayList(
                ImmutableList.<ObservableList<F>> builder()
                        .add(list)
                        .add(FXCollections.observableArrayList(suffixes)).build()));
    }

    @SafeVarargs
    public static <F> ConcatenatedList<F> create(ObservableList<F>... lists) {
        return new ConcatenatedList<F>(FXCollections.observableArrayList(lists));
    }

    @SafeVarargs
    public static <F> ConcatenatedList<F> create(List<F>... lists) {
        return new ConcatenatedList<F>(FXCollections.observableArrayList(
                Arrays.stream(lists).map(FXCollections::observableArrayList).collect(Collectors.toList())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        int sum = 0;
        int sourceIndex = -1;

        for (List<E> innerList : expandedValues) {
            if (index < sum) {
                break;
            }

            sum += innerList.size();
            sourceIndex++;
        }

        return sourceIndex;
    }

    /**
     * Finds the index of the first element in the source list at the given index position
     *
     * @param index The index in the source list
     * @return The index of the first element of the source index in this list
     * @apiNote This method is required to make Phoenicis compile with Java 9
     */
    public int getViewIndex(int index) {
        return getFirstIndex(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        E result = null;
        int start = 0;

        for (List<E> innerList : expandedValues) {
            if (start + innerList.size() > index) {
                result = innerList.get(index - start);

                break;
            } else {
                start += innerList.size();
            }
        }

        return result;
    }

    @Override
    public int size() {
        return expandedValues.stream().mapToInt(List::size).sum();
    }

    /**
     * Gets the first index in the target list belonging to an item in the list marked by the given
     * <code>sourceIndex</code>
     *
     * @param sourceIndex The index marking a list in the source list
     * @return The first index in the target list belonging to an item in the list marked by <code>sourceIndex</code>
     */
    private int getFirstIndex(int sourceIndex) {
        int position = 0;

        for (int i = 0; i < sourceIndex; i++) {
            final List<E> innerList = expandedValues.get(i);

            position += innerList.size();
        }

        return position;
    }

    /**
     * Gets the last index in the target list belonging to an item in the list marked by the given
     * <code>sourceIndex</code>
     *
     * @param sourceIndex The index marking a list in the source list
     * @return The last index in the target list belonging to an item in the list marked by <code>sourceIndex</code>
     */
    private int getLastIndexPlusOne(int sourceIndex) {
        int position = 0;

        for (int i = 0; i <= sourceIndex; i++) {
            final List<E> innerList = expandedValues.get(i);

            position += innerList.size();
        }

        return position;
    }

    @Override
    protected void permute(ListChangeListener.Change<? extends ObservableList<E>> c) {
        int from = c.getFrom();
        int to = c.getTo();

        int expandedFrom = getFirstIndex(from);
        int expandedTo = getLastIndexPlusOne(to - 1);

        if (to > from) {
            List<E> beforePermutation = expandedValues.stream().flatMap(List::stream).collect(Collectors.toList());
            List<List<E>> valuesClone = new ArrayList<>(expandedValues);

            for (int i = from; i < to; ++i) {
                valuesClone.set(i, expandedValues.get(c.getPermutation(i)));
            }

            this.expandedValues.clear();
            this.expandedValues.addAll(valuesClone);

            List<E> afterPermutation = expandedValues.stream().flatMap(List::stream).collect(Collectors.toList());

            int[] perm = beforePermutation.stream()
                    .mapToInt(afterPermutation::indexOf).toArray();

            nextPermutation(expandedFrom, expandedTo, perm);
        }
    }

    @Override
    protected void update(ListChangeListener.Change<? extends ObservableList<E>> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                int firstOldIndex = getFirstIndex(i);

                List<E> oldValues = expandedValues.get(i);

                ObservableList<E> newValues = getSource().get(i);

                expandedValues.set(i, new ArrayList<>(newValues));

                addUpdateListener(newValues);

                // more values were removed than added
                if (oldValues.size() > newValues.size()) {
                    for (int count = 0; count < newValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }

                    nextRemove(firstOldIndex, oldValues.subList(newValues.size(), oldValues.size()));
                }

                // more values were added than removed
                if (oldValues.size() < newValues.size()) {
                    for (int count = 0; count < oldValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }

                    nextAdd(firstOldIndex + oldValues.size(), firstOldIndex + newValues.size());
                }

                // all old values were replaces
                if (oldValues.size() == newValues.size()) {
                    for (int count = 0; count < oldValues.size(); count++) {
                        nextUpdate(firstOldIndex + count);
                    }
                }
            }
        }
    }

    @Override
    protected void addRemove(ListChangeListener.Change<? extends ObservableList<E>> c) {
        int from = c.getFrom();

        for (int index = from + c.getRemovedSize() - 1; index >= from; index--) {
            int firstOldIndex = getFirstIndex(index);

            nextRemove(firstOldIndex, expandedValues.remove(index));
        }

        for (int index = from; index < from + c.getAddedSize(); index++) {
            int lastOldIndex = getLastIndexPlusOne(index - 1);

            ObservableList<E> newValues = getSource().get(index);

            expandedValues.add(index, new ArrayList<>(newValues));

            addUpdateListener(newValues);

            nextAdd(lastOldIndex, lastOldIndex + newValues.size());
        }
    }

    private void addUpdateListener(final ObservableList<E> innerList) {
        innerList.addListener((ListChangeListener.Change<? extends E> change) -> {
            ObservableList<? extends E> activatorList = change.getList();

            beginChange();
            while (change.next()) {
                final int activatorListIndex = getSource().indexOf(innerList);

                if (change.wasPermutated()) {
                    int expandedFrom = getFirstIndex(activatorListIndex);

                    expandedValues.set(activatorListIndex, new ArrayList<>(activatorList));

                    nextPermutation(expandedFrom + change.getFrom(), expandedFrom + change.getTo(),
                            IntStream.range(change.getFrom(), change.getTo()).map(change::getPermutation).toArray());
                } else if (change.wasUpdated()) {
                    int expandedFrom = getFirstIndex(activatorListIndex);

                    expandedValues.set(activatorListIndex, new ArrayList<>(activatorList));

                    IntStream.range(expandedFrom + change.getFrom(), expandedFrom + change.getTo())
                            .forEach(this::nextUpdate);
                } else {
                    int expandedFrom = getFirstIndex(activatorListIndex);

                    expandedValues.set(activatorListIndex, new ArrayList<>(activatorList));

                    nextRemove(expandedFrom + change.getFrom(), change.getRemoved());
                    nextAdd(expandedFrom + change.getFrom(), expandedFrom + change.getFrom() + change.getAddedSize());
                }
            }
            endChange();
        });
    }
}
