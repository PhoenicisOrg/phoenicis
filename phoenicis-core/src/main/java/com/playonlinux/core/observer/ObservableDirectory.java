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

import java.io.File;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;

@Scan
public abstract class ObservableDirectory<T> extends ObservableDefaultImplementation<T> implements Service, AutoCloseable{
    @Inject
    static ServiceManager serviceManager;

    protected int checkInterval = 1000;
    protected File observedDirectory;

    protected void validate() {
        if(observedDirectory.exists() && !observedDirectory.isDirectory()) {
            throw new IllegalStateException(String.format("The file %s is not a valid directory",
                    observedDirectory.toString()));
        }
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public File getObservedDirectory() {
        return observedDirectory;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append(observedDirectory.getName())
                .append(checkInterval)
                .toString();
    }
    @Override
    public void shutdown() {
        this.deleteObservers();
    }

    @Override
    public void close() {
        serviceManager.unregister(this);
    }

}
