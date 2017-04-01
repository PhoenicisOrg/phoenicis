package org.phoenicis.javafx.views.common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by marc on 01.04.17.
 */
public class ExpandedListTest {
    @Test
    public void testListCreation() {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));
    }

    @Test
    public void testListAdd() {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        observableList.add(1, Arrays.asList("01", "02"));

        assertEquals(5, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("01", expandedList.get(1));
        assertEquals("02", expandedList.get(2));
        assertEquals("21", expandedList.get(3));
        assertEquals("22", expandedList.get(4));
    }

    @Test
    public void testListRemove1() {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        observableList.remove(0);

        assertEquals(2, expandedList.size());
        assertEquals("21", expandedList.get(0));
        assertEquals("22", expandedList.get(1));
    }

    @Test
    public void testListRemove2() {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        observableList.remove(1);

        assertEquals(1, expandedList.size());
        assertEquals("11", expandedList.get(0));
    }

    @Test
    public void testListRemove3() {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        observableList.remove(2);

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));
    }

    @Test
    public void testListUpdate() {
        ObservableList<List<String>> observableList = FXCollections.observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        observableList.set(2, Arrays.asList("31", "32", "33"));

        assertEquals(6, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));
        assertEquals("31", expandedList.get(3));
        assertEquals("32", expandedList.get(4));
        assertEquals("33", expandedList.get(5));
    }

    @Test
    public void testListPermutation() {
        SortedList<List<String>> observableList = FXCollections.<List<String>>observableArrayList(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList()).sorted();
        ExpandedList<String, List<String>> expandedList = new ExpandedList<>(observableList, Function.identity());

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        observableList.comparatorProperty().set(Comparator.comparing(o -> ((List<String>)o).size()).reversed());

        assertEquals(3, expandedList.size());
        assertEquals("21", expandedList.get(0));
        assertEquals("22", expandedList.get(1));
        assertEquals("11", expandedList.get(2));
    }
}
