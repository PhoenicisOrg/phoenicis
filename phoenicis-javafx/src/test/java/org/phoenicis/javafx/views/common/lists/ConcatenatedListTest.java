package org.phoenicis.javafx.views.common.lists;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;
import org.phoenicis.javafx.collections.ConcatenatedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConcatenatedListTest {
    @Test
    public void testListCreation() {
        ConcatenatedList<String> expandedList = ConcatenatedList
                .create(Arrays.asList("11"), Arrays.asList("21", "22"), Arrays.asList());
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);
    }

    @Test
    public void testListAdd() {
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList());
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.add(1, FXCollections.observableArrayList("01", "02"));

        assertEquals(Arrays.asList("11", "01", "02", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "01", "02", "21", "22"), actual);
    }

    @Test
    public void testListRemove1() {
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList());
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.remove(0);

        assertEquals(Arrays.asList("21", "22"), expandedList);
        assertEquals(Arrays.asList("21", "22"), actual);
    }

    @Test
    public void testListRemove2() {
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList());
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.remove(1);

        assertEquals(Arrays.asList("11"), expandedList);
        assertEquals(Arrays.asList("11"), actual);
    }

    @Test
    public void testListRemove3() {
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList());
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.remove(2);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);
    }

    @Test
    public void testListUpdate1() {
        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList());
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.set(2, FXCollections.observableArrayList("31", "32", "33"));

        assertEquals(Arrays.asList("11", "21", "22", "31", "32", "33"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31", "32", "33"), actual);
    }

    @Test
    public void testListUpdate2() {
        FilteredList<ObservableList<String>> observableList = FXCollections.observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList("31"))
                .filtered(value -> true);
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        observableList.setPredicate(value -> value.size() != 1);

        assertEquals(Arrays.asList("21", "22"), expandedList);
        assertEquals(Arrays.asList("21", "22"), actual);

        observableList.setPredicate(value -> true);

        assertEquals(Arrays.asList("11", "21", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31"), actual);
    }

    @Test
    public void testListPermutation() {
        SortedList<ObservableList<String>> observableList = FXCollections.<ObservableList<String>> observableArrayList(
                FXCollections.observableArrayList("11"),
                FXCollections.observableArrayList("21", "22"),
                FXCollections.observableArrayList())
                .sorted(Comparator.comparing(List::size));
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.comparatorProperty().set(Comparator.comparing(o -> ((List<String>) o).size()).reversed());

        assertEquals(Arrays.asList("21", "22", "11"), expandedList);
        assertEquals(Arrays.asList("21", "22", "11"), actual);
    }

    @Test
    public void testInnerListAdd() {
        ObservableList<String> list1 = FXCollections.observableArrayList("11");
        ObservableList<String> list2 = FXCollections.observableArrayList("21", "22");
        ObservableList<String> list3 = FXCollections.observableArrayList("31");

        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(list1, list2, list3);
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);

        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.add("23");

        assertEquals(Arrays.asList("11", "21", "22", "23", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "23", "31"), actual);
    }

    @Test
    public void testInnerListRemove() {
        ObservableList<String> list1 = FXCollections.observableArrayList("11");
        ObservableList<String> list2 = FXCollections.observableArrayList("21", "22");
        ObservableList<String> list3 = FXCollections.observableArrayList("31");

        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(list1, list2, list3);
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);

        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.remove(0);

        assertEquals(Arrays.asList("11", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "22", "31"), actual);
    }

    @Test
    public void testInnerListUpdate() {
        ObservableList<String> list1 = FXCollections.observableArrayList("11");
        ObservableList<String> list2 = FXCollections.observableArrayList("21", "22");
        ObservableList<String> list3 = FXCollections.observableArrayList("31");

        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(list1, list2, list3);
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);

        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.set(0, "20");

        assertEquals(Arrays.asList("11", "20", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "20", "22", "31"), actual);
    }

    @Test
    public void testInnerListPermute() {
        ObservableList<String> list1 = FXCollections.observableArrayList("11");
        SortedList<String> list2 = FXCollections.observableArrayList("21", "22").sorted();
        ObservableList<String> list3 = FXCollections.observableArrayList("31");

        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList(list1, list2, list3);
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);

        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22", "31"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.setComparator(Comparator.reverseOrder());

        assertEquals(Arrays.asList("11", "22", "21", "31"), expandedList);
        assertEquals(Arrays.asList("11", "22", "21", "31"), actual);
    }
}
