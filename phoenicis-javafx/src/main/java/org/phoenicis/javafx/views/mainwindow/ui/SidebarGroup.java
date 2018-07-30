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

package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a group of visual objects shown in the sidebar.
 * This group can have a title and other content consisting of multiple {@link Node}s.
 *
 * @since 26.04.17
 */
public class SidebarGroup extends VBox {
    /**
     * The title of this group.
     * This title if optional.
     */
    private Optional<String> title;

    /**
     * A list of nodes to be contained below the title in this group.
     */
    private List<Node> nodes;

    /**
     * Constructor
     */
    private SidebarGroup() {
        super();

        this.nodes = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param title The title string to be used for this group. If no title string is to be set null can be used.
     * @param content Zero or more nodes, which make up the content of this group.
     */
    public SidebarGroup(String title, Node... content) {
        this();

        this.title = Optional.ofNullable(title);
        this.nodes.addAll(Arrays.asList(content));

        this.getStyleClass().add("sidebarInside");

        this.refreshContent();
    }

    /**
     * Constructor
     *
     * @param content Zero or more nodes, which make up the content of this group.
     */
    public SidebarGroup(Node... content) {
        this(null, content);
    }

    /**
     * This method updates the title of this group.
     *
     * @param title The new title of this group
     */
    public void setTitle(String title) {
        this.title = Optional.ofNullable(title);

        this.refreshContent();
    }

    /**
     * This method updates the nodes inside this group.
     *
     * @param nodes The new nodes inside this group
     */
    public void setNodes(List<? extends Node> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);

        this.refreshContent();
    }

    /**
     * This method refreshes the visual components inside this group.
     * This method should be called whenever the title or the nodes of this group changes
     */
    private void refreshContent() {
        this.getChildren().clear();

        this.title.ifPresent(nameString -> {
            this.getChildren().add(new SidebarTitle(nameString));
        });

        this.getChildren().addAll(this.nodes);
    }
}