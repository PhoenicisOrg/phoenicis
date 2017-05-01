package org.phoenicis.javafx.views.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Created by marc on 26.04.17.
 */
public class AdhocListTest {

    @Test
    public void testListCreation() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Arrays.asList("3", "7", "1", "5"));
        AdhocList<String> mappedList = new AdhocList<>(observableList, "0");

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("1", mappedList.get(3));
        assertEquals("5", mappedList.get(4));
    }

    @Test
    public void testListAdd() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Arrays.asList("3", "7", "1", "5"));
        AdhocList<String> mappedList = new AdhocList<>(observableList, "0");

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("1", mappedList.get(3));
        assertEquals("5", mappedList.get(4));

        observableList.add("4");

        assertEquals(6, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("1", mappedList.get(3));
        assertEquals("5", mappedList.get(4));
        assertEquals("4", mappedList.get(5));
    }

    @Test
    public void testListRemove() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Arrays.asList("3", "7", "1", "5"));
        AdhocList<String> mappedList = new AdhocList<>(observableList, "0");

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("1", mappedList.get(3));
        assertEquals("5", mappedList.get(4));

        observableList.remove(2);

        assertEquals(4, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("5", mappedList.get(3));
    }

    @Test
    public void testListUpdate() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Arrays.asList("3", "7", "1", "5"));
        AdhocList<String> mappedList = new AdhocList<>(observableList, "0");

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("1", mappedList.get(3));
        assertEquals("5", mappedList.get(4));

        observableList.set(2, "4");

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("3", mappedList.get(1));
        assertEquals("7", mappedList.get(2));
        assertEquals("4", mappedList.get(3));
        assertEquals("5", mappedList.get(4));
    }

    @Test
    public void testListPermutation() {
        SortedList<String> sortedList = FXCollections.observableArrayList(Arrays.asList("3", "7", "1", "5")).sorted(Comparator.naturalOrder());
        AdhocList<String> mappedList = new AdhocList<>(sortedList, "0");

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("1", mappedList.get(1));
        assertEquals("3", mappedList.get(2));
        assertEquals("5", mappedList.get(3));
        assertEquals("7", mappedList.get(4));

        sortedList.comparatorProperty().set(Comparator.comparing(String::valueOf).reversed());

        assertEquals(5, mappedList.size());
        assertEquals("0", mappedList.get(0));
        assertEquals("7", mappedList.get(1));
        assertEquals("5", mappedList.get(2));
        assertEquals("3", mappedList.get(3));
        assertEquals("1", mappedList.get(4));
    }
}
