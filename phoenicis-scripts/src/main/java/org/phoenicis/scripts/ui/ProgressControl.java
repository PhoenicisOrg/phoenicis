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

package org.phoenicis.scripts.ui;

import org.phoenicis.entities.ProgressEntity;

import java.util.function.Consumer;

/**
 * Represents a progress control
 */
public interface ProgressControl extends Consumer<ProgressEntity> {
    /**
     * Set the percentage of the progress bar
     * @param value The value to set
     */
    void setProgressPercentage(double value);

    /**
     * The text of the progressbar
     * @param text The text to set
     */
    void setText(String text);

    @Override
    default void accept(ProgressEntity argument) {
        setProgressPercentage(argument.getPercent());
        setText(argument.getProgressText());
    }
}
