package org.phoenicis.javafx.collections;

import com.google.common.collect.ImmutableList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.collections.change.InitialisationChange;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An implementation of a concatenated {@link ObservableList}, which concatenates the values of multiple
 * {@link ObservableList}s into a single {@link ObservableList}
 *
 * @param <E> The instance type of the elements in the concatenated lists
 */
public class ConcatenatedList<E> extends PhoenicisTransformationList<E, ObservableList<? extends E>> {
    /**
     * A map linking the created {@link ListChangeListener} instances to their corresponding {@link ObservableList}.
     * This map is required to allow for the removal of the listener when an {@link ObservableList} is removed
     */
    private final Map<ObservableList<? extends E>, ListChangeListener<E>> innerListeners;

    /**
     * An internal copy of the source list of lists
     */
    private final List<List<? extends E>> expandedValues;

    /**
     * Constructor
     *
     * @param source A list of lists which should be concatenated
     */
    public ConcatenatedList(ObservableList<? extends ObservableList<? extends E>> source) {
        super(source);

        this.innerListeners = new HashMap<>();
        this.expandedValues = source.stream().map(ArrayList::new).collect(Collectors.toList());

        source.forEach(this::addUpdateListener);
        fireChange(new InitialisationChange<>(0, size(), this));
    }

    /**
     * Creates a new {@link ConcatenatedList} concatenating the given prefix values and the given
     * {@link ObservableList list}. In the created {@link ConcatenatedList} the prefix values are in
     * front of the {@link ObservableList list}
     *
     * @param list The list
     * @param prefixes The prefix values
     * @param <F> The instance type of the elements in the list and the prefix values
     * @return A new {@link ConcatenatedList} containing all elements in given list and the prefix values
     */
    @SafeVarargs
    public static <F> ConcatenatedList<F> createPrefixList(ObservableList<? extends F> list, F... prefixes) {
        return new ConcatenatedList<>(FXCollections.observableArrayList(
                ImmutableList.<ObservableList<? extends F>> builder()
                        .add(FXCollections.observableArrayList(prefixes))
                        .add(list).build()));
    }

    /**
     * Creates a new {@link ConcatenatedList} concatenating the given {@link ObservableList list} and the given suffix
     * values. In the created {@link ConcatenatedList} the suffix values are located after the
     * {@link ObservableList list}
     *
     * @param list The list
     * @param suffixes The suffix values
     * @param <F> The instance type of the elements in the list and the suffix values
     * @return A new {@link ConcatenatedList} containing all elements in given list and the suffix values
     */
    @SafeVarargs
    public static <F> ConcatenatedList<F> createSuffixList(ObservableList<? extends F> list, F... suffixes) {
        return new ConcatenatedList<>(FXCollections.observableArrayList(
                ImmutableList.<ObservableList<? extends F>> builder()
                        .add(list)
                        .add(FXCollections.observableArrayList(suffixes)).build()));
    }

    /**
     * Creates a new {@link ConcatenatedList} with the given {@link ObservableList[] lists}
     *
     * @param lists The lists, which should be concatenated
     * @param <F> The instance type of the elements in the to be concatenated lists
     * @return A new {@link ConcatenatedList} containing all elements in the given lists
     */
    @SafeVarargs
    public static <F> ConcatenatedList<F> create(ObservableList<? extends F>... lists) {
        return new ConcatenatedList<>(FXCollections.observableArrayList(lists));
    }

    /**
     * Creates a new {@link ConcatenatedList} with the given {@link List[] lists}
     *
     * @param lists The lists, which should be concatenated
     * @param <F> The instance type of the elements in the to be concatenated lists
     * @return A new {@link ConcatenatedList} containing all elements in the given lists
     */
    @SafeVarargs
    public static <F> ConcatenatedList<F> create(List<? extends F>... lists) {
        return new ConcatenatedList<>(FXCollections.observableArrayList(
                Arrays.stream(lists).map(FXCollections::observableArrayList).collect(Collectors.toList())));
    }

    /**
     * Creates a new {@link ConcatenatedList} with the given {@link ObservableList} of lists
     *
     * @param list The lists, which should be concatenated
     * @param <F> The instance type of the elements in the to be concatenated lists
     * @return A new {@link ConcatenatedList} containing all elements in the given lists
     */
    public static <F> ConcatenatedList<F> create(ObservableList<? extends List<? extends F>> list) {
        return new ConcatenatedList<>(new MappedList<>(list, FXCollections::observableArrayList));
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
            final List<? extends E> innerList = expandedValues.get(i);

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
            final List<? extends E> innerList = expandedValues.get(i);

            position += innerList.size();
        }

        return position;
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

        for (List<? extends E> innerList : expandedValues) {
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

        for (List<? extends E> innerList : expandedValues) {
            if (start + innerList.size() > index) {
                result = innerList.get(index - start);

                break;
            } else {
                start += innerList.size();
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return expandedValues.stream().mapToInt(List::size).sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void permute(ListChangeListener.Change<? extends ObservableList<? extends E>> change) {
        final int from = change.getFrom();
        final int to = change.getTo();

        final int expandedFrom = getFirstIndex(from);
        final int expandedTo = getLastIndexPlusOne(to - 1);

        if (to > from) {
            final List<? extends E> beforePermutation = expandedValues.stream().flatMap(List::stream)
                    .collect(Collectors.toList());
            final List<List<? extends E>> valuesClone = new ArrayList<>(expandedValues);

            for (int i = from; i < to; ++i) {
                valuesClone.set(i, expandedValues.get(change.getPermutation(i)));
            }

            this.expandedValues.clear();
            this.expandedValues.addAll(valuesClone);

            final List<? extends E> afterPermutation = expandedValues.stream().flatMap(List::stream)
                    .collect(Collectors.toList());

            final int[] perm = beforePermutation.stream()
                    .mapToInt(afterPermutation::indexOf).toArray();

            nextPermutation(expandedFrom, expandedTo, perm);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void update(ListChangeListener.Change<? extends ObservableList<? extends E>> change) {
        final int from = change.getFrom();
        final int to = change.getTo();

        for (int i = from; i < to; i++) {
            final ObservableList<? extends E> oldValues = change.getRemoved().get(i - from);

            // remove the update listener form the old observable list
            removeUpdateListener(oldValues);

            final ObservableList<? extends E> newValues = change.getAddedSubList().get(i - from);

            expandedValues.set(i, new ArrayList<>(newValues));

            // add an update listener to the new observable list
            addUpdateListener(newValues);

            final int firstOldIndex = getFirstIndex(i);

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addRemove(ListChangeListener.Change<? extends ObservableList<? extends E>> change) {
        final int from = change.getFrom();

        for (int removedIndex = change.getRemovedSize() - 1; removedIndex >= 0; removedIndex--) {
            final int index = from + removedIndex;

            final ObservableList<? extends E> oldValues = change.getRemoved().get(removedIndex);

            // remove the update listener form the old observable list
            removeUpdateListener(oldValues);

            final int firstOldIndex = getFirstIndex(index);

            nextRemove(firstOldIndex, expandedValues.remove(index));
        }

        for (int addedIndex = 0; addedIndex < change.getAddedSize(); addedIndex++) {
            final int index = from + addedIndex;

            final ObservableList<? extends E> newValues = change.getAddedSubList().get(addedIndex);

            expandedValues.add(index, new ArrayList<>(newValues));

            // add an update listener to the new observable list
            addUpdateListener(newValues);

            final int lastOldIndex = getLastIndexPlusOne(index - 1);

            nextAdd(lastOldIndex, lastOldIndex + newValues.size());
        }
    }

    /**
     * Adds a {@link ListChangeListener} to the given {@link ObservableList innerList}.
     * This {@link ListChangeListener} listens to changes made to the given list.
     *
     * @param innerList The {@link ObservableList} to which the {@link ListChangeListener} is added
     */
    private void addUpdateListener(final ObservableList<? extends E> innerList) {
        final ListChangeListener<E> innerListener = (ListChangeListener.Change<? extends E> change) -> {
            final ObservableList<? extends E> activatorList = change.getList();

            beginChange();
            while (change.next()) {
                final int activatorListIndex = getSource().indexOf(innerList);

                expandedValues.set(activatorListIndex, new ArrayList<>(activatorList));

                final int expandedFrom = getFirstIndex(activatorListIndex);

                if (change.wasPermutated()) {
                    nextPermutation(expandedFrom + change.getFrom(), expandedFrom + change.getTo(),
                            IntStream.range(change.getFrom(), change.getTo()).map(change::getPermutation).toArray());
                } else if (change.wasUpdated()) {
                    IntStream.range(expandedFrom + change.getFrom(), expandedFrom + change.getTo())
                            .forEach(this::nextUpdate);
                } else {
                    nextRemove(expandedFrom + change.getFrom(), change.getRemoved());
                    nextAdd(expandedFrom + change.getFrom(), expandedFrom + change.getFrom() + change.getAddedSize());
                }
            }
            endChange();
        };

        innerListeners.put(innerList, innerListener);

        innerList.addListener(innerListener);
    }

    /**
     * Removes the {@link ListChangeListener} from the given {@link ObservableList innerList}
     *
     * @param innerList The {@link ObservableList} from which the {@link ListChangeListener} should be removed
     */
    private void removeUpdateListener(final ObservableList<? extends E> innerList) {
        final ListChangeListener<E> innerListener = innerListeners.get(innerList);

        innerList.removeListener(innerListener);

        innerListeners.remove(innerList);
    }
}
