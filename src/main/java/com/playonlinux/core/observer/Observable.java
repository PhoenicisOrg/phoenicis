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

/**
 * Defines an observable (see Observable pattern)
 * 
 * @param <A>
 *            The type of the argument
 */
public interface Observable<A> {
    /**
     * Register an obverser
     * 
     * @param observer
     *            the observer to add
     */
    void addObserver(Observer observer);

    /**
     * Register an observer
     * 
     * @param observer
     *            the observer to add
     */
    void deleteObserver(Observer observer);

    /**
     * Notify all the observers
     */
    void notifyObservers();

    /**
     * Notify an observer with an argument
     * 
     * @param arg
     *            the argument to pass
     */
    void notifyObservers(A arg);

    /**
     * Delete an observer
     */
    void deleteObservers();

    /**
     * Get the number of observers
     * 
     * @return the number of observers
     */
    int countObservers();
}
