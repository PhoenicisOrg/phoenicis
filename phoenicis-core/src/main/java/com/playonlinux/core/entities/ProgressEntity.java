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

package com.playonlinux.core.entities;

import com.playonlinux.core.dto.DTO;

/**
 * Represent a progress state that will be sent to the UI.
 * The UI will be able to represent this progress state (progressbar, etc...)
 */
public class ProgressEntity implements DTO {
    private final ProgressState state;
    private final double percent;
    private final String progressText;

    public ProgressEntity(ProgressState state){
        this(state, 0, null);
    }
    
    public ProgressEntity(ProgressState state, double percent){
        this(state, percent, null);
    }
    
    public ProgressEntity(ProgressState state, double percent, String progressText) {
        this.state = state;
        this.percent = percent;
        this.progressText = progressText;
    }

    public ProgressState getState() {
        return state;
    }

    public double getPercent() {
        return percent;
    }

    public String getProgressText() {
        return progressText;
    }

    @Override
    public String toString() {
        return this.state.name();
    }
}
