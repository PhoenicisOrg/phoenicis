package org.phoenicis.javafx.views.common.lists;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.junit.Assert;
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));

        observableList.add(1, FXCollections.observableArrayList("01", "02"));

        assertEquals(5, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("01", expandedList.get(1));
        assertEquals("02", expandedList.get(2));
        assertEquals("21", expandedList.get(3));
        assertEquals("22", expandedList.get(4));

        assertEquals(5, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("01", actual.get(1));
        assertEquals("02", actual.get(2));
        assertEquals("21", actual.get(3));
        assertEquals("22", actual.get(4));
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));

        observableList.remove(0);

        assertEquals(2, expandedList.size());
        assertEquals("21", expandedList.get(0));
        assertEquals("22", expandedList.get(1));

        assertEquals(2, actual.size());
        assertEquals("21", actual.get(0));
        assertEquals("22", actual.get(1));
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));

        observableList.remove(1);

        assertEquals(1, expandedList.size());
        assertEquals("11", expandedList.get(0));

        assertEquals(1, actual.size());
        assertEquals("11", actual.get(0));
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));

        observableList.remove(2);

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));

        observableList.set(2, FXCollections.observableArrayList("31", "32", "33"));

        assertEquals(6, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));
        assertEquals("31", expandedList.get(3));
        assertEquals("32", expandedList.get(4));
        assertEquals("33", expandedList.get(5));

        assertEquals(6, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));
        assertEquals("31", actual.get(3));
        assertEquals("32", actual.get(4));
        assertEquals("33", actual.get(5));
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

        assertEquals(4, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));
        assertEquals("31", expandedList.get(3));

        assertEquals(4, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));
        assertEquals("31", actual.get(3));

        observableList.setPredicate(value -> value.size() != 1);

        assertEquals(2, expandedList.size());
        assertEquals("21", expandedList.get(0));
        assertEquals("22", expandedList.get(1));

        assertEquals(2, actual.size());
        assertEquals("21", actual.get(0));
        assertEquals("22", actual.get(1));

        observableList.setPredicate(value -> true);

        assertEquals(4, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));
        assertEquals("31", expandedList.get(3));

        assertEquals(4, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));
        assertEquals("31", actual.get(3));
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

        assertEquals(3, expandedList.size());
        assertEquals("11", expandedList.get(0));
        assertEquals("21", expandedList.get(1));
        assertEquals("22", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("11", actual.get(0));
        assertEquals("21", actual.get(1));
        assertEquals("22", actual.get(2));

        observableList.comparatorProperty().set(Comparator.comparing(o -> ((List<String>) o).size()).reversed());

        assertEquals(3, expandedList.size());
        assertEquals("21", expandedList.get(0));
        assertEquals("22", expandedList.get(1));
        assertEquals("11", expandedList.get(2));

        assertEquals(3, actual.size());
        assertEquals("21", actual.get(0));
        assertEquals("22", actual.get(1));
        assertEquals("11", actual.get(2));
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

        Assert.assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.add("23");

        Assert.assertEquals(Arrays.asList("11", "21", "22", "23", "31"), actual);
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

        Assert.assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.remove(0);

        Assert.assertEquals(Arrays.asList("11", "22", "31"), actual);
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

        Assert.assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.set(0, "20");

        Assert.assertEquals(Arrays.asList("11", "20", "22", "31"), actual);
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

        Assert.assertEquals(Arrays.asList("11", "21", "22", "31"), actual);

        list2.setComparator(Comparator.reverseOrder());

        Assert.assertEquals(Arrays.asList("11", "22", "21", "31"), actual);
    }
}
