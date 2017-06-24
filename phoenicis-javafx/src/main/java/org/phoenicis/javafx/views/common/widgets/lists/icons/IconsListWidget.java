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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ListWidget} for a icons list containing a miniature icon and a title for each list entry.
 *
 * @author marc
 * @since 15.05.17
 */
public final class IconsListWidget<E> extends ScrollPane implements ListWidget<E> {
    /**
     * The container of this IconsListWidget that contains the content of this list widget
     */
    private final FlowPane content;

    /**
     * A set of selected items
     */
    private Set<IconsListElement<E>> selectedItems;

    /**
     * A list containing all items to be shown inside this {@link ListWidget}
     */
    private ObservableList<E> items;

    /**
     * A list containing all mapped {@link IconsListElement} objects to the <code>items</code>
     */
    private ObservableList<IconsListElement<E>> mappedElements;

    /**
     * Constructor
     *
     * @param converter         A converter function used to convert a value of input type <code>E</code> to {@link IconsListElement}
     * @param setOnMouseClicked An event listener function to be called when a list element has been selected/clicked
     */
    public IconsListWidget(Function<E, IconsListElement<E>> converter, BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.getStyleClass().add("iconListWidget");

        this.selectedItems = new HashSet<>();

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<>(items, value -> {
            IconsListElement<E> newElement = converter.apply(value);

            newElement.setOnMouseClicked(event -> {
                this.deselectAll();
                setOnMouseClicked.accept(value, event);
                this.select(value);
            });

            return newElement;
        });

        this.content = new FlowPane();

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
    public void bind(ObservableList<E> list) {
        Bindings.bindContent(this.items, list);
    }

    @Override
    public void deselectAll() {
        this.selectedItems.forEach(element -> element.setSelected(false));
        this.selectedItems.clear();
    }

    @Override
    public void select(E selectedItem) {
        IconsListElement<E> item = this.mappedElements.get(this.items.indexOf(selectedItem));

        item.setSelected(true);

        this.selectedItems.add(item);
    }

    @Override
    public Collection<E> getSelectedItems() {
        return this.selectedItems.stream().map(index -> items.get(mappedElements.indexOf(index)))
                .collect(Collectors.toList());
    }
}
