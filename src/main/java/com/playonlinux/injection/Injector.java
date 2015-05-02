package com.playonlinux.injection;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Injector {
    private String packageName;

    public Injector(String packageName) {
        this.packageName = packageName;
    }

    public Set<Class<?>> getComponentClasses() {
        Reflections reflections = new Reflections(this.packageName);
        Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);
        return componentClasses;
    }

    public <T extends Annotation> Boolean isAnnotatedWith(Field field, Class<T> annotation) {
        return field.getAnnotation(annotation) != null;
    }

    public <T extends Annotation> Boolean isAnnotatedWith(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation) != null;
    }

    public <T extends Annotation> List<Field> getAnnotatedFields(Class<?> annotatedClass, Class<T> annotation) {
        List<Field> fields = new ArrayList();
        for(Field field: annotatedClass.getDeclaredFields()) {
            if(isAnnotatedWith(field, annotation)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> annotatedClass, Class<T> annotation) {
        List<Method> methods = new ArrayList();
        for(Method method: annotatedClass.getDeclaredMethods()) {
            if(isAnnotatedWith(method, annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

}
