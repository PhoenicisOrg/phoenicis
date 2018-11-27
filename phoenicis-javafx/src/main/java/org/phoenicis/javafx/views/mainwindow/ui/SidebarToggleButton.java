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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;

/**
 * This class represents a toggle button in the sidebar.
 */
public class SidebarToggleButton extends ToggleButton {
    /**
     * The text shown in the button
     */
    private final String name;

    /**
     * Constructor
     *
     * @param name The text to be shown in this toggle button
     */
    public SidebarToggleButton(String name) {
        super(name);
        this.name = name;
        this.getStyleClass().add("sidebarButton");
    }

    /**
     * This method is called whenever the {@link SidebarToggleButton} has been clicked.
     * It is overriden to ensure that the button only fires an event ({@link javafx.event.ActionEvent} or
     * {@link javafx.scene.input.MouseEvent})
     * if it is either currently not selected or it isn't part of a {@link javafx.scene.control.ToggleGroup}.
     *
     * @see ToggleButton
     */
    @Override
    public void fire() {
        /**
         * Taken from:
         * https://bitbucket.org/controlsfx/controlsfx/issues/84/segmented-button-control-clicking-on
         */
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }

    /**
     * This method returns the shown text of this button.
     *
     * @return The currently shown text of this button.
     */
    public String getName() {
        return name;
    }
}
