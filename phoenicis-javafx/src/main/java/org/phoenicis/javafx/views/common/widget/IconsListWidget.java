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

package org.phoenicis.javafx.views.common.widget;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class IconsListWidget<E> extends ScrollPane implements ListWidget<ListWidgetEntry<E>> {
    private final FlowPane content;

    private Element selectedItem;

    private ObservableList<ListWidgetEntry<E>> items;
    private ObservableList<Element<E>> mappedElements;

    public IconsListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.getStyleClass().add("rightPane");

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<Element<E>, ListWidgetEntry<E>>(items, value -> {
            Element newElement = new Element(value);

            newElement.setOnMouseClicked(event -> {
                setOnMouseClicked.accept(value.getItem(), event);
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
        this.mappedElements.forEach(element -> element.getStyleClass().remove("selected"));
        this.selectedItem = null;
    }

    @Override
    public void select(ListWidgetEntry<E> selectedItem) {
        this.selectedItem = this.mappedElements.get(this.items.indexOf(selectedItem));
        this.selectedItem.getStyleClass().add("selected");
    }

    @Override
    public ListWidgetEntry<E> getSelectedItem() {
        return this.items.get(this.mappedElements.indexOf(selectedItem));
    }

    public static class Element<E> extends VBox {
        private final String elementName;

        private final E value;

        private final StaticMiniature miniature;

        public Element(ListWidgetEntry<E> item) {
            super();

            this.value = item.getItem();
            this.elementName = item.getTitle();
            this.miniature = new StaticMiniature(item.getIconUri());

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

            final Label label = new Label(elementName);
            label.getStyleClass().add("miniatureText");

            this.getChildren().add(miniature);
            this.getChildren().add(label);

            final Tooltip tooltip = new Tooltip(elementName);
            Tooltip.install(miniature, tooltip);
        }

        public E getValue() {
            return this.value;
        }

        public String getName() {
            return elementName;
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

            builder.append(value, that.value);

            return builder.isEquals();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();

            builder.append(value);

            return builder.toHashCode();
        }
    }
}
