package org.phoenicis.javafx.views.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Created by marc on 01.04.17.
 */
public class PhoenicisFilteredListTest {
    @Test
    public void testListCreation() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(1, 2, 4, 3));
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(observableList, i -> i % 2 == 0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));
    }

    @Test
    public void testListAdd() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(1, 2, 4, 3));
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(observableList, i -> i % 2 == 0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        observableList.add(0);

        assertEquals(3, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));
        assertEquals(0, (int) filteredList.get(2));

        observableList.add(5);

        assertEquals(3, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));
        assertEquals(0, (int) filteredList.get(2));
    }

    @Test
    public void testListRemove() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(1, 2, 4, 3));
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(observableList, i -> i % 2 == 0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        observableList.remove(0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        observableList.remove(0);

        assertEquals(1, filteredList.size());
        assertEquals(4, (int) filteredList.get(0));
    }

    @Test
    public void testListUpdate() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(1, 2, 4, 3));
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(observableList, i -> i % 2 == 0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        observableList.set(1, 1);

        assertEquals(1, filteredList.size());
        assertEquals(4, (int) filteredList.get(0));

        observableList.set(3, 0);

        assertEquals(2, filteredList.size());
        assertEquals(4, (int) filteredList.get(0));
        assertEquals(0, (int) filteredList.get(1));
    }

    @Test
    public void testListPermutation() {
        SortedList<Integer> sortedList = FXCollections.observableList(Arrays.asList(1, 2, 4, 3))
                .sorted(Comparator.naturalOrder());
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(sortedList, i -> i % 2 == 0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        sortedList.comparatorProperty().set(Comparator.comparing(String::valueOf).reversed());

        assertEquals(2, filteredList.size());
        assertEquals(4, (int) filteredList.get(0));
        assertEquals(2, (int) filteredList.get(1));
    }

    @Test
    public void testClear() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(1, 2, 4, 3));
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(observableList, i -> i % 2 == 0);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        observableList.clear();

        assertEquals(0, filteredList.size());
    }

    private int moduloResult = 0;

    @Test
    public void testTrigger() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(1, 2, 4, 3));
        PhoenicisFilteredList<Integer> filteredList = new PhoenicisFilteredList<>(observableList,
                i -> i % 2 == moduloResult);

        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        this.moduloResult = 1;

        // we should get the same result, because we didn't call trigger yet
        assertEquals(2, filteredList.size());
        assertEquals(2, (int) filteredList.get(0));
        assertEquals(4, (int) filteredList.get(1));

        filteredList.trigger();

        assertEquals(2, filteredList.size());
        assertEquals(1, (int) filteredList.get(0));
        assertEquals(3, (int) filteredList.get(1));
    }
}
