/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx.views.common.widgets.lists.icons;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * A {@link ListWidget} for a icons list containing a miniature icon and a title for each list entry.
 *
 * @author marc
 * @since 15.05.17
 */
public final class IconsListWidget<E> extends ScrollPane implements ListWidget<ListWidgetEntry<E>> {
    /**
     * The container of this IconsListWidget that contains the content of this list widget
     */
    private final FlowPane content;

    /**
     * A set of selected items
     */
    private Set<IconsListElement<E>> selectedItems;

    /**
     * A list containing all {@link ListWidgetEntry} objects to be shown inside this {@link ListWidget}
     */
    private ObservableList<ListWidgetEntry<E>> items;

    /**
     * A list containing all mapped {@link IconsListElement} objects to the <code>items</code>
     */
    private ObservableList<IconsListElement<E>> mappedElements;

    /**
     * Constructor
     *
     * @param setOnMouseClicked An event listener function to be called when a list element has been selected/clicked
     */
    public IconsListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.getStyleClass().add("rightPane");

        this.selectedItems = new HashSet<>();

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<IconsListElement<E>, ListWidgetEntry<E>>(items, value -> {
            IconsListElement newElement = new IconsListElement(value);

            newElement.setOnMouseClicked(event -> {
                this.deselectAll();
                setOnMouseClicked.accept(value.getItem(), event);
                this.select(value);
            });

            return newElement;
        });

        this.content = new FlowPane();
        this.content.getStyleClass().addAll("miniatureList");

        this.content.setPrefHeight(0);
        this.content.setPrefWidth(0);

        this.content.prefWidthProperty().bind(this.widthProperty());

        this.setContent(this.content);

        Bindings.bindContent(content.getChildren(), this.mappedElements);

        this.setCache(true);
        this.setCacheHint(CacheHint.QUALITY);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
    }

    @Override
    public void bind(ObservableList<ListWidgetEntry<E>> list) {
        Bindings.bindContent(this.items, list);
    }

    @Override
    public void deselectAll() {
        this.selectedItems.forEach(element -> element.getStyleClass().remove("selected"));
        this.selectedItems.clear();
    }

    @Override
    public void select(ListWidgetEntry<E> selectedItem) {
        IconsListElement<E> item = this.mappedElements.get(this.items.indexOf(selectedItem));

        if (!item.getStyleClass().contains("selected")) {
            item.getStyleClass().add("selected");
        }

        this.selectedItems.add(item);
    }

    @Override
    public Collection<ListWidgetEntry<E>> getSelectedItems() {
        return this.selectedItems.stream().map(index -> items.get(mappedElements.indexOf(index)))
                .collect(Collectors.toList());
    }
}
