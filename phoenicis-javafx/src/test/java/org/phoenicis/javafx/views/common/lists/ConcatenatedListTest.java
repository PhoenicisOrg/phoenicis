package org.phoenicis.javafx.views.common.lists;

import com.google.common.collect.ImmutableList;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.junit.Test;
import org.phoenicis.javafx.collections.ConcatenatedList;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ConcatenatedListTest {
    @Test
    public void testListCreation() {
        ConcatenatedList<String> expandedList = ConcatenatedList
                .create(Collections.singletonList("11"), Arrays.asList("21", "22"), Collections.emptyList());
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);
    }

    @Test
    public void testListAdd() {
        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList()).build());
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
    public void testListRemoveFirstList() {
        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList()).build());
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
    public void testListRemoveMiddleList() {
        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList()).build());
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.remove(1);

        assertEquals(Collections.singletonList("11"), expandedList);
        assertEquals(Collections.singletonList("11"), actual);
    }

    @Test
    public void testListRemoveLastList() {
        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList()).build());
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
    public void testListUpdateLastList() {
        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList()).build());
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
    public void testListUpdateViaFilteredList() {
        FilteredList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList("31")).build())
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
        SortedList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(FXCollections.observableArrayList("11"))
                        .add(FXCollections.observableArrayList("21", "22"))
                        .add(FXCollections.observableArrayList()).build())
                .sorted(Comparator.comparing(List::size));
        ConcatenatedList<String> expandedList = new ConcatenatedList<>(observableList);
        List<String> actual = new ArrayList<>();

        Bindings.bindContent(actual, expandedList);

        assertEquals(Arrays.asList("11", "21", "22"), expandedList);
        assertEquals(Arrays.asList("11", "21", "22"), actual);

        observableList.comparatorProperty().set(Comparator.<List<String>, Integer> comparing(List::size).reversed());

        assertEquals(Arrays.asList("21", "22", "11"), expandedList);
        assertEquals(Arrays.asList("21", "22", "11"), actual);
    }

    @Test
    public void testInnerListAdd() {
        ObservableList<String> list1 = FXCollections.observableArrayList("11");
        ObservableList<String> list2 = FXCollections.observableArrayList("21", "22");
        ObservableList<String> list3 = FXCollections.observableArrayList("31");

        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(list1).add(list2).add(list3).build());
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

        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(list1).add(list2).add(list3).build());
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

        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(list1).add(list2).add(list3).build());
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

        ObservableList<ObservableList<String>> observableList = FXCollections
                .observableArrayList(ImmutableList.<ObservableList<String>> builder()
                        .add(list1).add(list2).add(list3).build());
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
