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
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.icons.StaticMiniature;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class IconsListWidget<E> extends ScrollPane implements ListWidget<ListWidgetEntry<E>> {
    private final FlowPane content;

    private Set<Element<E>> selectedItems;

    private ObservableList<ListWidgetEntry<E>> items;
    private ObservableList<Element<E>> mappedElements;

    public IconsListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.getStyleClass().add("rightPane");

        this.selectedItems = new HashSet<>();

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<Element<E>, ListWidgetEntry<E>>(items, value -> {
            Element newElement = new Element(value);

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
        Element<E> item = this.mappedElements.get(this.items.indexOf(selectedItem));

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

    public static class Element<E> extends VBox {
        private ListWidgetEntry<E> item;

        public Element(ListWidgetEntry<E> item) {
            super();

            this.item = item;

            this.getStyleClass().add("miniatureListElement");
            this.setAlignment(Pos.CENTER);

            this.widthProperty().addListener((observable, oldValue, newValue) -> {
                final Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
                this.setClip(clip);
            });

            this.heightProperty().addListener((observable, oldValue, newValue) -> {
                final Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
                this.setClip(clip);
            });

            final Label label = new Label(item.getTitle());
            label.getStyleClass().add("miniatureText");

            StaticMiniature miniature = new StaticMiniature(item.getIconUri());

            this.getChildren().add(miniature);
            this.getChildren().add(label);

            final Tooltip tooltip = new Tooltip(item.getTitle());
            Tooltip.install(miniature, tooltip);

            if (!item.isEnabled()) {
                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(-1);
                this.setEffect(grayscale);
            }
        }

        public E getValue() {
            return this.item.getItem();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Element<?> that = (Element<?>) o;

            EqualsBuilder builder = new EqualsBuilder();

            builder.append(item, that.item);

            return builder.isEquals();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();

            builder.append(item);

            return builder.toHashCode();
        }
    }
}
