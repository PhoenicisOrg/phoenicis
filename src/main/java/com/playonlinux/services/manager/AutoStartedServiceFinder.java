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

package com.playonlinux.services.manager;

import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutoStartedServiceFinder {
    private String packageName = "com.playonlinux";

    public Map<String, Class<?>> findClasses() {
        final HashMap<String, Class<?>> results = new HashMap<>();
        final Reflections reflections = new Reflections(this.packageName);
        final Set<Class<?>> classes
                = reflections.getTypesAnnotatedWith(AutoStartedService.class);

        for(Class<?> clazz: classes) {
            final String serviceName = clazz.getDeclaredAnnotation(AutoStartedService.class).name();
            final Class<?> serviceType = clazz.getDeclaredAnnotation(AutoStartedService.class).type();

            if(StringUtils.isBlank(serviceName)) {
                results.put(serviceType.getName(), clazz);
            } else {
                results.put(serviceName, clazz);
            }
        }

        return results;
    }
}
