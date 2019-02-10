package org.phoenicis.javafx.utils;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.function.Function;

public class CollectionBindings {
    public static <I, O> ObservableList<O> mapToList(ObservableValue<I> property,
            Function<I, ? extends Collection<O>> converter) {
        final ObservableList<O> result = FXCollections.observableArrayList();

        property.addListener((Observable invalidation) -> {
            final I input = property.getValue();

            if (input != null) {
                result.setAll(converter.apply(input));
            } else {
                result.clear();
            }
        });

        return result;
    }
}
