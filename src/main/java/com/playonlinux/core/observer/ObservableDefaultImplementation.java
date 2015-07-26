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

package com.playonlinux.core.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ObservableDefaultImplementation<ARG>
        implements Observable<ARG> {
    private final List<Observer> observers;
    private ARG lastArgument;

    public ObservableDefaultImplementation() {
        observers = new ArrayList<>();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        Objects.requireNonNull(o);
        if (!observers.contains(o)) {
            observers.add(o);
        }

        if(lastArgument != null) {
            o.update(this, lastArgument);
        }
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }

    @Override
    public synchronized void notifyObservers(ARG arg) {
        lastArgument = arg;
        for(Observer observer: observers) {
            observer.update(this, arg);
        }
    }

    @Override
    public synchronized void deleteObservers() {
        observers.clear();
    }

    @Override
    public synchronized int countObservers() {
        return observers.size();
    }
}
