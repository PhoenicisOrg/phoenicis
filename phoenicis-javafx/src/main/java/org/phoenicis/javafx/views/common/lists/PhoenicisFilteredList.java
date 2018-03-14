package org.phoenicis.javafx.views.common.lists;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.*;
import java.util.function.Predicate;

/**
 * A filtered observable list taking a {@link Predicate} to filter elements of a given input {@link ObservableList}.
 * This class is based on the implementation of {@link FilteredList}, included in javafx, and extends its functionality with a trigger method.
 * This trigger method can be used to recheck the all elements included in the source list to only marks the elements as invalid,
 * that were previously added and now aren't, or the other way.
 *
 * @param <E> The type of the elements contained in the filtered list
 * @author Marc Arndt
 * @see FilteredList
 */
public class PhoenicisFilteredList<E> extends PhoenicisTransformationList<E, E> {
    private int[] filtered;
    private int size;

    private SortHelper helper;
    private static final Predicate ALWAYS_TRUE = t -> true;

    /**
     * Constructs a new FilteredList wrapper around the source list.
     * The provided predicate will match the elements in the source list that will be visible.
     * If the predicate is null, all elements will be matched and the list is equal to the source list.
     *
     * @param source    the source list
     * @param predicate the predicate to match the elements or null to match all elements.
     */
    public PhoenicisFilteredList(@NamedArg("source") ObservableList<E> source,
            @NamedArg("predicate") Predicate<? super E> predicate) {
        super(source);
        filtered = new int[source.size() * 3 / 2 + 1];
        if (predicate != null) {
            setPredicate(predicate);
        } else {
            for (size = 0; size < source.size(); size++) {
                filtered[size] = size;
            }
        }
    }

    /**
     * Constructs a new FilteredList wrapper around the source list.
     * This list has an "always true" predicate, containing all the elements
     * of the source list.
     * <p>
     * This constructor might be useful if you want to bind {@link #predicateProperty()}
     * of this list.
     *
     * @param source the source list
     */
    public PhoenicisFilteredList(@NamedArg("source") ObservableList<E> source) {
        this(source, null);
    }

    /**
     * The predicate that will match the elements that will be in this FilteredList.
     * Elements not matching the predicate will be filtered-out.
     * Null predicate means "always true" predicate, all elements will be matched.
     */
    private ObjectProperty<Predicate<? super E>> predicate;

    public final ObjectProperty<Predicate<? super E>> predicateProperty() {
        if (predicate == null) {
            predicate = new ObjectPropertyBase<Predicate<? super E>>() {
                @Override
                protected void invalidated() {
                    refilter();
                }

                @Override
                public Object getBean() {
                    return PhoenicisFilteredList.this;
                }

                @Override
                public String getName() {
                    return "predicate";
                }

            };
        }
        return predicate;
    }

    public final Predicate<? super E> getPredicate() {
        return predicate == null ? null : predicate.get();
    }

    public final void setPredicate(Predicate<? super E> predicate) {
        predicateProperty().set(predicate);
    }

    private Predicate<? super E> getPredicateImpl() {
        if (getPredicate() != null) {
            return getPredicate();
        }
        return ALWAYS_TRUE;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
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
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return getSource().get(filtered[index]);
    }

    @Override
    public int getSourceIndex(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return filtered[index];
    }

    /**
     * Taken from: https://github.com/teamfx/openjfx-9-dev-rt/blob/master/modules/javafx.base/src/main/java/javafx/collections/transformation/FilteredList.java
     *
     * @apiNote This method is required to make Phoenicis compile with Java 9
     */
    public int getViewIndex(int index) {
        return Arrays.binarySearch(filtered, 0, size, index);
    }

    private SortHelper getSortHelper() {
        if (helper == null) {
            helper = new SortHelper();
        }
        return helper;
    }

    private int findPosition(int p) {
        if (filtered.length == 0) {
            return 0;
        }
        if (p == 0) {
            return 0;
        }
        int pos = Arrays.binarySearch(filtered, 0, size, p);
        if (pos < 0) {
            pos = ~pos;
        }
        return pos;
    }

    @SuppressWarnings("unchecked")
    private void ensureSize(int size) {
        if (filtered.length < size) {
            int[] replacement = new int[size * 3 / 2 + 1];
            System.arraycopy(filtered, 0, replacement, 0, this.size);
            filtered = replacement;
        }
    }

    private void updateIndexes(int from, int delta) {
        for (int i = from; i < size; ++i) {
            filtered[i] += delta;
        }
    }

    protected void permute(ListChangeListener.Change<? extends E> c) {
        int from = findPosition(c.getFrom());
        int to = findPosition(c.getTo());

        if (to > from) {
            for (int i = from; i < to; ++i) {
                filtered[i] = c.getPermutation(filtered[i]);
            }

            int[] perm = getSortHelper().sort(filtered, from, to);
            nextPermutation(from, to, perm);
        }
    }

    protected void addRemove(ListChangeListener.Change<? extends E> c) {
        Predicate<? super E> pred = getPredicateImpl();
        ensureSize(getSource().size());
        final int from = findPosition(c.getFrom());
        final int to = findPosition(c.getFrom() + c.getRemovedSize());

        // Mark the nodes that are going to be removed
        for (int i = from; i < to; ++i) {
            nextRemove(from, c.getRemoved().get(filtered[i] - c.getFrom()));
        }

        // Update indexes of the sublist following the last element that was removed
        updateIndexes(to, c.getAddedSize() - c.getRemovedSize());

        // Replace as many removed elements as possible
        int fpos = from;
        int pos = c.getFrom();

        ListIterator<? extends E> it = getSource().listIterator(pos);
        for (; fpos < to && it.nextIndex() < c.getTo();) {
            if (pred.test(it.next())) {
                filtered[fpos] = it.previousIndex();
                nextAdd(fpos, fpos + 1);
                ++fpos;
            }
        }

        if (fpos < to) {
            // If there were more removed elements than added
            System.arraycopy(filtered, to, filtered, fpos, size - to);
            size -= to - fpos;
        } else {
            // Add the remaining elements
            while (it.nextIndex() < c.getTo()) {
                if (pred.test(it.next())) {
                    System.arraycopy(filtered, fpos, filtered, fpos + 1, size - fpos);
                    filtered[fpos] = it.previousIndex();
                    nextAdd(fpos, fpos + 1);
                    ++fpos;
                    ++size;
                }
                ++pos;
            }
        }
    }

    protected void update(ListChangeListener.Change<? extends E> c) {
        Predicate<? super E> pred = getPredicateImpl();
        ensureSize(getSource().size());
        int sourceFrom = c.getFrom();
        int sourceTo = c.getTo();
        int filterFrom = findPosition(sourceFrom);
        int filterTo = findPosition(sourceTo);
        ListIterator<? extends E> it = getSource().listIterator(sourceFrom);
        int pos = filterFrom;
        while (pos < filterTo || sourceFrom < sourceTo) {
            E el = it.next();
            if (pos < size && filtered[pos] == sourceFrom) {
                if (!pred.test(el)) {
                    nextRemove(pos, el);
                    System.arraycopy(filtered, pos + 1, filtered, pos, size - pos - 1);
                    --size;
                    --filterTo;
                } else {
                    nextUpdate(pos);
                    ++pos;
                }
            } else {
                if (pred.test(el)) {
                    nextAdd(pos, pos + 1);
                    System.arraycopy(filtered, pos, filtered, pos + 1, size - pos);
                    filtered[pos] = sourceFrom;
                    ++size;
                    ++pos;
                    ++filterTo;
                }
            }
            sourceFrom++;
        }
    }

    @SuppressWarnings("unchecked")
    private void refilter() {
        ensureSize(getSource().size());
        List<E> removed = null;
        if (hasListeners()) {
            removed = new ArrayList<>(this);
        }
        size = 0;
        int i = 0;
        Predicate<? super E> pred = getPredicateImpl();
        for (Iterator<? extends E> it = getSource().iterator(); it.hasNext();) {
            final E next = it.next();
            if (pred.test(next)) {
                filtered[size++] = i;
            }
            ++i;
        }
        if (hasListeners()) {
            fireChange(new NonIterableChange.GenericAddRemoveChange<>(0, size, removed, this));
        }
    }

    /**
     * Triggers a check of all elements inside the source list to check if they still match the predicate function.
     * Only if they are now contained inside this filtered list and were not contained previously,
     * or the other way around, will a {@link javafx.collections.ListChangeListener.Change} event be triggered for the element.
     */
    public void trigger() {
        Predicate<? super E> pred = getPredicateImpl();

        beginChange();

        int sourceFrom = 0;
        int sourceTo = getSource().size();
        int filterFrom = findPosition(sourceFrom);
        int filterTo = findPosition(sourceTo);

        ListIterator<? extends E> it = getSource().listIterator(sourceFrom);
        int pos = filterFrom;
        while (pos < filterTo || sourceFrom < sourceTo) {
            E el = it.next();
            if (pos < size && filtered[pos] == sourceFrom) {
                if (!pred.test(el)) {
                    nextRemove(pos, el);
                    System.arraycopy(filtered, pos + 1, filtered, pos, size - pos - 1);
                    --size;
                    --filterTo;
                } else {
                    ++pos;
                }
            } else {
                if (pred.test(el)) {
                    nextAdd(pos, pos + 1);
                    System.arraycopy(filtered, pos, filtered, pos + 1, size - pos);
                    filtered[pos] = sourceFrom;
                    ++size;
                    ++pos;
                    ++filterTo;
                }
            }
            sourceFrom++;
        }

        endChange();
    }

}
