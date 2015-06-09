/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.common.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

/**
 * ArrayList that can be observed for changes.
 *
 * @param <T> Type of the items within this list.
 */
public class ObservableArrayList<T> extends Observable implements List<T>, com.playonlinux.common.api.list.ObservableList<T> {

    private List<T> list = new ArrayList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public void swapContents(Collection<T> newContent) {
        list.clear();
        list.addAll(newContent);
        this.fireUpdate();
    }

    @Override
    public T[] toArray() {
        return (T[]) list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return list.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        boolean changed = list.add(t);
        if (changed) {
            this.fireUpdate();
        }
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        boolean changed = list.remove(o);
        if (changed) {
            this.fireUpdate();
        }
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = list.addAll(collection);
        if (changed) {
            this.fireUpdate();
        }
        return changed;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) {
        boolean changed = list.addAll(i, collection);
        if (changed) {
            this.fireUpdate();
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = list.removeAll(collection);
        if (changed) {
            this.fireUpdate();
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = list.retainAll(collection);
        if (changed) {
            this.fireUpdate();
        }
        return changed;
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public T set(int i, T t) {
        T oldItem = list.set(i, t);
        this.fireUpdate();
        return oldItem;
    }

    @Override
    public void add(int i, T t) {
        list.add(i, t);
        this.fireUpdate();
    }

    @Override
    public T remove(int i) {
        T oldItem = list.remove(i);
        this.fireUpdate();
        return oldItem;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return list.listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i1) {
        return list.subList(i, i1);
    }


    private void fireUpdate() {
        this.setChanged();
        this.notifyObservers();
    }

}
