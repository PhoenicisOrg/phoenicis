package org.phoenicis.javafx.collections;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

public abstract class PhoenicisTransformationList<E, F> extends TransformationList<E, F> {

    protected PhoenicisTransformationList(ObservableList<? extends F> source) {
        super(source);
    }

    protected abstract void permute(ListChangeListener.Change<? extends F> c);

    protected abstract void update(ListChangeListener.Change<? extends F> c);

    protected abstract void addRemove(ListChangeListener.Change<? extends F> c);

    @Override
    protected void sourceChanged(ListChangeListener.Change<? extends F> c) {
        beginChange();
        while (c.next()) {
            if (c.wasPermutated()) {
                permute(c);
            } else if (c.wasUpdated()) {
                update(c);
            } else {
                addRemove(c);
            }
        }
        endChange();
    }
}
