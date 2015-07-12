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

package com.playonlinux.utils;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.services.manager.Service;
import com.playonlinux.utils.observer.AbstractObservableImplementation;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;

public abstract class AbstractObservableDirectory extends AbstractObservableImplementation implements Service {
    protected int checkInterval = 1000;
    protected File observedDirectory;

    protected void validate() throws PlayOnLinuxException {
        if(!observedDirectory.exists()) {
            throw new PlayOnLinuxException(String.format("The directory %s does not exist",
                    observedDirectory.toString()));
        }
        if(!observedDirectory.isDirectory()) {
            throw new PlayOnLinuxException(String.format("The file %s is not a valid directory",
                    observedDirectory.toString()));
        }
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    @Override
    public abstract void start();

    public abstract void stop();

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
        this.stop();
    }


}
