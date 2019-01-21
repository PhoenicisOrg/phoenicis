package org.phoenicis.javafx.views.common.lists;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;
import org.phoenicis.javafx.collections.MappedList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Created by marc on 01.04.17.
 */
public class MappedListTest {
    @Test
    public void testListCreation() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);
    }

    @Test
    public void testListAdd() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);

        observableList.add(0);

        assertEquals(5, mappedList.size());
        assertEquals(Arrays.asList("3", "7", "1", "5", "0"), mappedList);
    }

    @Test
    public void testListRemove() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);

        observableList.remove(2);

        assertEquals(Arrays.asList("3", "7", "5"), mappedList);
    }

    @Test
    public void testListUpdate() {
        ObservableList<Integer> observableList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);

        observableList.set(2, 4);

        assertEquals(Arrays.asList("3", "7", "4", "5"), mappedList);
    }

    @Test
    public void testListPermutation() {
        SortedList<Integer> sortedList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5))
                .sorted(Comparator.naturalOrder());
        MappedList<String, Integer> mappedList = new MappedList<>(sortedList, String::valueOf);

        assertEquals(Arrays.asList("1", "3", "5", "7"), mappedList);

        sortedList.comparatorProperty().set(Comparator.comparing(String::valueOf).reversed());

        assertEquals(Arrays.asList("7", "5", "3", "1"), mappedList);
    }

    @Test
    public void testMapperChange() {
        ObservableList<Integer> observableList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);

        mappedList.setMapper(i -> i + "!");

        assertEquals(Arrays.asList("3!", "7!", "1!", "5!"), mappedList);
    }

    @Test
    public void testMapperChangeToNull() {
        ObservableList<Integer> observableList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);

        mappedList.setMapper(null);

        assertEquals(Collections.emptyList(), mappedList);
    }

    @Test
    public void testMapperChangeFromNull() {
        ObservableList<Integer> observableList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList);

        assertEquals(Collections.emptyList(), mappedList);

        mappedList.setMapper(String::valueOf);

        assertEquals(Arrays.asList("3", "7", "1", "5"), mappedList);
    }
}
