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

/**
 * Represents a configuration file.
 * Two configuration files cannot be loaded at the same time, seems they are using the static context.
 */
public abstract class AbstractConfiguration implements AutoCloseable {
    private static volatile Semaphore staticContextLock = new Semaphore(1);

    protected boolean strictLoadingPolicy = true;
    
    protected abstract String definePackage();

    /**
     * If this is set to true, the injector will throw an exception if a non defined bean in the config file
     * is being injected. Otherwise, the object will stay to null (useful for a testing context)
     *
     * @param strictLoadingPolicy should be true if it enabled, false if is not
     */
    public void setStrictLoadingPolicy(Boolean strictLoadingPolicy) {
        this.strictLoadingPolicy = strictLoadingPolicy;
    }

    /**
     * Loads a configuration file. If another configuration file is already loaded, blocks until it is closed.
     * @throws InjectionException if the load process is interrupted
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
