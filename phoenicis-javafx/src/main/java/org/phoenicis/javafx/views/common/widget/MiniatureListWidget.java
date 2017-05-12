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
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class MiniatureListWidget<E> extends ScrollPane {
    private final Pane content;
    private Element selectedItem;

    private ObservableList<E> items;
    private ObservableList<Element<E>> mappedElements;

    private MiniatureListWidget(Pane content, Function<E, Element> converter,
            BiConsumer<Element<E>, MouseEvent> setOnMouseClicked) {
        super(content);

        this.content = content;
        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<Element<E>, E>(items, value -> {
            Element newElement = converter.apply(value);

            newElement.setOnMouseClicked(event -> {
                unselectAll();
                setOnMouseClicked.accept(newElement, event);
                select(newElement);
            });

            return newElement;
        });

        Bindings.bindContent(content.getChildren(), this.mappedElements);

        this.getStyleClass().add("rightPane");

        this.content.getStyleClass().addAll("miniatureList");

        this.getChildren().add(this.content);

        this.setCache(true);
        this.setCacheHint(CacheHint.QUALITY);

        this.content.prefWidthProperty().bind(this.widthProperty());
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
    }

    /**
     * Creates a new @class MiniatureListWidget of type @type <T>.
     *
     * @param converter         A converter function that converts values of type @type T to Element
     * @param setOnMouseClicked A mouse listener function, that is called whenever a user clicks on an element.
     *                          This listener function receives the element, which has been clicked, and the event as parameters
     * @param <T>               The type of items to be added to this MiniatureListWidget
     * @return
     */
    public static <T> MiniatureListWidget<T> create(Function<T, Element> converter,
            BiConsumer<Element<T>, MouseEvent> setOnMouseClicked) {
        return new MiniatureListWidget<T>(new FlowPane(), converter, setOnMouseClicked);
    }

    public ObservableList<E> getItems() {
        return this.items;
    }

    public void setItems(List<E> items) {
        this.items.setAll(items);
    }

    public List<Element<E>> getElements() {
        return this.mappedElements;
    }

    public void unselectAll() {
        getElements().forEach(element -> element.getStyleClass().remove("selected"));
        this.selectedItem = null;
    }

    public void select(Element selectedItem) {
        selectedItem.getStyleClass().add("selected");
        this.selectedItem = selectedItem;
    }

    public Element getSelectedItem() {
        return selectedItem;
    }

    public static class Element<E> extends VBox {
        private final String elementName;

        private final E value;

        public Element(E value, String elementName, Node miniature) {
            super();

            this.getStyleClass().add("miniatureListElement");

            this.setAlignment(Pos.CENTER);
            this.elementName = elementName;
            this.value = value;

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

        public Element(String appsItem, Node miniature) {
            this(null, appsItem, miniature);
        }

        public Element(String elementName) {
            this(elementName, new StaticMiniature());
        }

        public static Element<ApplicationDTO> create(ApplicationDTO application) {
            return new Element<ApplicationDTO>(application, application.getName(), application.getMiniatures().isEmpty()
                    ? new StaticMiniature() : new StaticMiniature(application.getMiniatures().get(0)));
        }

        public static Element<ShortcutDTO> create(ShortcutDTO shortcut) {
            return new Element<ShortcutDTO>(shortcut, shortcut.getName(), shortcut.getMiniature() == null
                    ? new StaticMiniature() : new StaticMiniature(shortcut.getMiniature()));
        }

        public static Element<EngineVersionDTO> create(EngineVersionDTO engineVersion, boolean installed) {
            Element<EngineVersionDTO> result = new Element<EngineVersionDTO>(engineVersion, engineVersion.getVersion(),
                    new StaticMiniature(StaticMiniature.WINE_MINIATURE));

            if (!installed) {
                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(-1);
                result.setEffect(grayscale);
            }

            return result;
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
