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

package com.playonlinux.core.injection;

import java.util.Map;
import java.util.concurrent.Semaphore;


public abstract class AbstractConfiguration implements AutoCloseable {

    /**
     * If this is set to true, the injector will throw an exception if a non defined bean in the config file
     * is being injected. Otherwise, the object will stay to null (useful for a testing context)
     */
    boolean strictLoadingPolicy = true;
    
    protected abstract String definePackage();
    private volatile static Semaphore staticContextLock = new Semaphore(1);

    public void setStrictLoadingPolicy(Boolean strictLoadingPolicy) {
        this.strictLoadingPolicy = strictLoadingPolicy;
    }

    public final void load() throws InjectionException {
        try {
            staticContextLock.acquire();
        } catch (InterruptedException e) {
            throw new InjectionException("The operation was canceled", e);
        }

        Injector injector = new Injector(definePackage());
        Map<Class<?>, Object> beans = injector.loadAllBeans(this);
        injector.injectAllBeans(strictLoadingPolicy, beans);
    }

    @Override
    public void close() {
        staticContextLock.release();
    }
}
