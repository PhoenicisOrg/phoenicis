package org.phoenicis.javafx.views.common;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

import java.util.stream.IntStream;

/**
 * Created by marc on 26.04.17.
 */
public class AdhocList<E> extends TransformationList<E, E> {
    private E[] others;

    public AdhocList(ObservableList<? extends E> source, E... others) {
        super(source);

        this.others = others;
    }

    @Override
    public int getSourceIndex(int index) {
        return index - others.length;
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

    private void permutate(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            int[] perm = IntStream.range(0, size()).toArray();

            for (int i = from; i < to; ++i) {
                perm[i + others.length] = c.getPermutation(i) + others.length;
            }

            nextPermutation(from, to, perm);
        }
    }

    private void update(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        if (to > from) {
            for (int i = from; i < to; ++i) {
                nextUpdate(i + others.length);
            }
        }
    }

    private void addRemove(ListChangeListener.Change<? extends E> c) {
        int from = c.getFrom();
        int to = c.getTo();

        nextRemove(from + others.length, c.getRemoved());
        nextAdd(from + others.length, from + c.getAddedSize() + others.length);
    }
}
