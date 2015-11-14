/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.ui.api;

/**
 * Represents a UI controller
 */
public interface Controller {
    /**
     * Start the UI
     */
    void startApplication();

    /**
     * Creates a {@link SetupWindow} instance
     * 
     * @param title
     *            The title of the setupwindow
     * @return The created instance
     */
    SetupWindow createSetupWindowGUIInstance(String title);

    /**
     * Creates a {@link UIMessageSender}
     * 
     * @param <T>
     *            The type returned by messages
     * @return The created instance
     */
    <T> UIMessageSender<T> createUIMessageSender();
}
