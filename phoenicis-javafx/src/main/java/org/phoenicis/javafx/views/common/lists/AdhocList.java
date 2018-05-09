package org.phoenicis.javafx.views.common.lists;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

/**
 * This class combines an {@link ObservableList} and a number of non exchangeable objects called <code>others</code> to
 * a single {@link ObservableList}.
 * These <code>others</code> are prepended to the given {@link ObservableList}.
 *
 * @author marc
 * @since 26.04.17
 */
public class AdhocList<E> extends PhoenicisTransformationList<E, E> {
    /**
     * An array containing a number of objects that are prepended to the {@link ObservableList} <code>source</code>
     */
    private E[] others;

    /**
     * Constructor
     *
     * @param source An observable list which should be part of this list
     * @param others A number of objects of the same type as <code>source</code> that should be prepended to
     *            <code>source</code>
     */
    public AdhocList(ObservableList<? extends E> source, E... others) {
        super(source);

        this.others = others;
    }

    @Override
    public int getSourceIndex(int index) {
        return index - others.length;
    }

    /**
     * Returns the index of the element, in the source list on the given index, inside this list
     *
     * @param index The index of the element in the source list
     * @return The index of the element in this list
     * @apiNote This method is required to make Phoenicis compile with Java 9
     */
    public int getViewIndex(int index) {
        return index + others.length;
    }

    @Override
    public E get(int index) {
        if (index < others.length) {
            return others[index];
        } else {
            return getSource().get(index - others.length);
        }
    }

    @Override
    public int size() {
        return others.length + getSource().size();
    }

    protected void permute(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            int[] perm = IntStream.range(0, size()).toArray();

            for (int i = from; i < to; ++i) {
                perm[i + others.length] = c.getPermutation(i) + others.length;
            }

            nextPermutation(others.length + from, others.length + to, perm);
        }
    }

    protected void update(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                nextUpdate(i + others.length);
            }
        }
    }

    protected void addRemove(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();

        nextRemove(from + others.length, c.getRemoved());
        nextAdd(from + others.length, from + c.getAddedSize() + others.length);
    }
}
