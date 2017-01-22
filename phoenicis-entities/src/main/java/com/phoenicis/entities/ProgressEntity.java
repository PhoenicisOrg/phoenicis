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

package com.phoenicis.entities;

/**
 * Represent a progress state that will be sent to the UI. The UI will be able
 * to represent this progress state (progressbar, etc...)
 */
public class ProgressEntity {
    private final ProgressState state;
    private final double percent;

    private final String progressText;

    private ProgressEntity(Builder builder) {
        this.state = builder.state;
        this.percent = builder.percent;
        this.progressText = builder.progressText;
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

    public static class Builder {
        private ProgressState state;
        private double percent;
        private String progressText;

        public Builder() {
            // Empty Constructor
        }

        public Builder(ProgressEntity other) {
            state = other.state;
            percent = other.percent;
            progressText = other.progressText;
        }

        public Builder withState(ProgressState state) {
            this.state = state;
            return this;
        }

        public Builder withPercent(double percent) {
            this.percent = percent;
            return this;
        }

        public Builder withProgressText(String progressText) {
            this.progressText = progressText;
            return this;
        }

        public ProgressEntity build() {
            return new ProgressEntity(this);
        }
    }
}
