package org.phoenicis.javafx.views.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by marc on 01.04.17.
 */
public class MappedListTest {
    @Test
    public void testListAdd() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, i -> String.valueOf(i));

        assertEquals(4, mappedList.size());
        assertEquals("3", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("1", mappedList.get(2));
        assertEquals("5", mappedList.get(3));

        observableList.add(0);

        assertEquals(5, mappedList.size());
        assertEquals("3", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("1", mappedList.get(2));
        assertEquals("5", mappedList.get(3));
        assertEquals("0", mappedList.get(4));
    }

    @Test
    public void testListRemove() {
        ObservableList<Integer> observableList = FXCollections.observableArrayList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, i -> String.valueOf(i));

        assertEquals(4, mappedList.size());
        assertEquals("3", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("1", mappedList.get(2));
        assertEquals("5", mappedList.get(3));

        observableList.remove(2);

        assertEquals(3, mappedList.size());
        assertEquals("3", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("5", mappedList.get(2));
    }

    @Test
    public void testListUpdate() {
        ObservableList<Integer> observableList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5));
        MappedList<String, Integer> mappedList = new MappedList<>(observableList, i -> String.valueOf(i));

        assertEquals(4, mappedList.size());
        assertEquals("3", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("1", mappedList.get(2));
        assertEquals("5", mappedList.get(3));

        observableList.set(2, 4);

        assertEquals(4, mappedList.size());
        assertEquals("3", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("4", mappedList.get(2));
        assertEquals("5", mappedList.get(3));
    }

    @Test
    public void testListPermutation() {
        SortedList<Integer> sortedList = FXCollections.observableList(Arrays.asList(3, 7, 1, 5)).sorted();
        MappedList<String, Integer> mappedList = new MappedList<>(sortedList, i -> String.valueOf(i));

        assertEquals(4, mappedList.size());
        assertEquals("1", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("5", mappedList.get(2));
        assertEquals("7", mappedList.get(3));

        sortedList.comparatorProperty().set(Comparator.comparing(String::valueOf).reversed());

        assertEquals(4, mappedList.size());
        assertEquals("7", mappedList.get(0));
        assertEquals("5", mappedList.get(1));
        assertEquals("3", mappedList.get(2));
        assertEquals("1", mappedList.get(3));
    }
}
