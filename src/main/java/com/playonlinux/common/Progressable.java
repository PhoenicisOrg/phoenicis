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

package com.playonlinux.common;

import java.util.Observable;

public class Progressable extends Observable {
    public enum State {
        READY,
        RUNNING,
        SUCCESS,
        FAILED
    }

    private State state;
    private float percentage;

    private boolean progressing;

    protected void setState(State state) {
        this.state = state;
        this.setChanged();
        this.notifyObservers();
    }
    public boolean isProgressing() {
        return state == State.RUNNING;
    }

    public synchronized float getPercentage() {
        return percentage;
    }

    protected synchronized void setPercentage(float percentage) {
        this.percentage = percentage;
        this.setChanged();
        this.notifyObservers();
    }
}
