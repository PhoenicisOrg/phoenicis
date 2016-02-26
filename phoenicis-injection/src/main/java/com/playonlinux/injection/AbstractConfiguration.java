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

package com.playonlinux.injection;

import java.util.Map;
import java.util.concurrent.Semaphore;

import lombok.Setter;

/**
 * Represents a configuration file. Two configuration files cannot be loaded at
 * the same time, seems they are using the static context.
 */
public abstract class AbstractConfiguration implements AutoCloseable {
    private static volatile Semaphore staticContextLock = new Semaphore(1);

    @Setter
    protected boolean strictLoadingPolicy = true;

    protected abstract String definePackage();

    /**
     * Loads a configuration file. If another configuration file is already
     * loaded, blocks until it is closed.
     * 
     * @throws InjectionException
     *             if the load process is interrupted
     */
    public final void load() throws InjectionException {
        try {
            staticContextLock.acquire();
        } catch (InterruptedException e) {
            throw new InjectionException("The operation was canceled", e);
        }

        final Injector injector = new Injector(definePackage());
        Map<Class<?>, Object> beans = injector.loadAllBeans(this);
        injector.injectAllBeans(strictLoadingPolicy, beans);
    }

    /**
     * Close a configuration file
     */
    @Override
    public void close() {
        final Injector injector = new Injector(definePackage());
        injector.cleanUpAllBeans();
        staticContextLock.release();
    }
}
