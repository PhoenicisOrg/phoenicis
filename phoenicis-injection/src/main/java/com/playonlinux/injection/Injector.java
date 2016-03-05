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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Injector {
    private final String packageName;

    public Injector(String packageName) {
        this.packageName = packageName;
    }

    public Set<Class<?>> getComponentClasses() {
        Reflections reflections = new Reflections(this.packageName);
        return reflections.getTypesAnnotatedWith(Scan.class);
    }

    public <T extends Annotation> Boolean isAnnotatedWith(Field field, Class<T> annotation) {
        return field.getAnnotation(annotation) != null;
    }

    public <T extends Annotation> Boolean isAnnotatedWith(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation) != null;
    }

    public <T extends Annotation> List<Field> getAnnotatedFields(Class<?> annotatedClass, Class<T> annotation) {
        List<Field> fields = new ArrayList<>();
        for (Field field : annotatedClass.getDeclaredFields()) {
            if (isAnnotatedWith(field, annotation)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> annotatedClass, Class<T> annotation) {
        List<Method> methods = new ArrayList<>();
        for (Method method : annotatedClass.getDeclaredMethods()) {
            if (isAnnotatedWith(method, annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public Map<Class<?>, Object> loadAllBeans(AbstractConfiguration configFile) throws InjectionException {
        List<Method> methods = this.getAnnotatedMethods(configFile.getClass(), Bean.class);

        Map<Class<?>, Object> beans = new HashMap<>();

        for (Method method : methods) {
            method.setAccessible(true);
            try {
                beans.put(method.getReturnType(), method.invoke(configFile));
            } catch (IllegalAccessException e) {
                throw new InjectionException("Unable to inject dependencies (IllegalAccessException).", e);
            } catch (InvocationTargetException e) {
                throw new InjectionException("Unable to inject dependencies (InvocationTargetException)", e);
            }
        }
        return beans;
    }

    public void injectAllBeans(Boolean strictLoadingPolicy, Map<Class<?>, Object> beans) throws InjectionException {
        log.debug("Loading dependencies...");
        Set<Class<?>> componentClasses = this.getComponentClasses();

        for (Class<?> componentClass : componentClasses) {
            List<Field> fields = this.getAnnotatedFields(componentClass, Inject.class);
            for (Field field : fields) {
                injectOneBean(strictLoadingPolicy, beans, field, componentClass);
            }
        }

    }

    private void injectOneBean(Boolean strictLoadingPolicy, Map<Class<?>, Object> beans, Field field,
            Class<?> componentClass) throws InjectionException {
        if (strictLoadingPolicy && !beans.containsKey(field.getType())) {
            log.debug("Loaded beans:");
            log.debug(beans.toString());
            throw new InjectionException(String.format("Unable to inject %s on class %s. Check your config file",
                    field.getType().toString(), componentClass.getName()), null);
        } else if (beans.containsKey(field.getType())) {
            try {
                field.setAccessible(true);
                field.set(null, beans.get(field.getType()));
            } catch (Exception e) {
                String injectErrorString = String.format("Unable to inject %s on class %s. Error while injecting.",
                        field.getType().toString(), componentClass.getName());

                log.error(injectErrorString, e);
                throw new InjectionException(injectErrorString, e);
            }
        }
    }

    public void cleanUpAllBeans() {
        Set<Class<?>> componentClasses = this.getComponentClasses();

        for (Class<?> componentClass : componentClasses) {
            List<Field> fields = this.getAnnotatedFields(componentClass, Inject.class);
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    field.set(null, null);
                } catch (IllegalAccessException e) {
                    log.warn(String.format("Unable to clean %s on class %s. Error while injecting.",
                            field.getType().toString(), componentClass.getName()), e);
                }
            }
        }
    }
}
