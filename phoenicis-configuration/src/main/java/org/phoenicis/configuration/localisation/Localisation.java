/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.configuration.localisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public final class Localisation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Localisation.class);
    private static final LocalisationHelper localisationHelper = new LocalisationHelper();
    private static I18n i18n = null;

    // This is a static class
    private Localisation() {

    }

    private static I18n getI18n() {
        // init only once
        if (i18n == null) {
            i18n = I18nFactory.getI18n(Localisation.class, "Messages");
        }
        return i18n;
    }

    /**
     * add a ResourceBundle containing additional translations for the localisation
     * - typically used to add translations for the scripts
     * - only one additional ResourceBundle can be set at a time (calling the method again will override the old ResourceBundle)
     *
     * @param resourceBundle Resource bundle
     */
    public static void setAdditionalTranslations(PropertiesResourceBundle resourceBundle) {
        resourceBundle.setParent(getI18n().getResources());
        getI18n().setResources(resourceBundle);
    }

    private static String trString(String text) {
        return text == null ? null : getI18n().tr(text);
    }

    public static String tr(String text, Object o1) {
        return text == null ? null : getI18n().tr(text, o1);
    }

    public static String tr(String text, Object o1, Object o2) {
        return text == null ? null : getI18n().tr(text, o1, o2);
    }

    public static String tr(String text, Object o1, Object o2, Object o3) {
        return text == null ? null : getI18n().tr(text, o1, o2, o3);
    }

    public static String tr(String text, Object o1, Object o2, Object o3, Object o4) {
        return text == null ? null : getI18n().tr(text, o1, o2, o3, o4);
    }

    public static String tr(String text, Object[] objects) {
        return text == null ? null : getI18n().tr(text, objects);
    }

    /**
     * Translates the given Translatable object, by creating a new instance of it with its corresponding translations
     *
     * @return translated object
     */
    public static <T> T tr(T translatable) {
        if (translatable == null) {
            return null;
        }

        if (translatable instanceof String) {
            @SuppressWarnings("unchecked")
            final T translateResult = (T) trString(translatable.toString());
            return translateResult;
        }

        if (translatable instanceof List) {
            @SuppressWarnings("unchecked")
            final T translateResult = (T) trList((List) translatable);
            return translateResult;
        }

        if (translatable instanceof Set) {
            @SuppressWarnings("unchecked")
            final T translateResult = (T) trSet((Set) translatable);
            return translateResult;
        }

        final Class<?> clazz = translatable.getClass();
        final Optional<Constructor<?>> translateCreator = findTranslateCreator(clazz);
        final Optional<Class<?>> translateBuilder = findTranslateBuilder(clazz);

        try {
            if (translateCreator.isPresent()) {
                return trFromTranslateCreator(translatable, translateCreator.get());
            }

            if (translateBuilder.isPresent()) {
                return trFromTranslateBuilder(translatable, translateBuilder.get());
            }

        } catch (ReflectiveOperationException e) {
            LOGGER.warn("Error while translating " + translatable + ". Falling back to the original value", e);
        }

        LOGGER.warn(translatable + " is neither a String, neither a collection, neither translatable. Ignoring");
        return translatable;
    }

    /**
     * Tells if a class is translatable or not
     * @param clazz The class to test
     * @return A boolean
     */
    public static boolean isTranslatable(Class<?> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType() == Translatable.class) {
                return true;
            }
        }

        return false;
    }

    private static <T> List<T> trList(List<T> translatable) {
        return translatable.stream().map(Localisation::tr).collect(Collectors.toList());
    }

    private static <T> Set<T> trSet(Set<T> translatable) {
        return translatable.stream().map(Localisation::tr).collect(Collectors.toSet());
    }

    private static <T> T trFromTranslateCreator(T translatable, Constructor<?> translateCreator)
            throws ReflectiveOperationException {
        final List<Object> parameters = new ArrayList<>();

        for (Parameter parameter : translateCreator.getParameters()) {
            final Method getter = findSuitableGetter(translatable, parameter);
            final Object getterResult = getter.invoke(translatable);

            if (shouldBeTranslated(getter)) {
                parameters.add(tr(getterResult));
            } else {
                parameters.add(getterResult);
            }
        }

        @SuppressWarnings("unchecked")
        T result = (T) translateCreator.newInstance(parameters.toArray());

        return result;
    }

    private static <T> T trFromTranslateBuilder(T translatable, Class<?> builderClass)
            throws ReflectiveOperationException {
        final Object builder = builderClass.newInstance();

        for (Method method : builderClass.getMethods()) {
            if (method.getName().startsWith("with")) {

                final Method getter = translatable.getClass()
                        .getMethod(localisationHelper.getterNameFromBuilderMethod(method));
                final Object getterResult = getter.invoke(translatable);

                if (shouldBeTranslated(getter)) {
                    method.invoke(builder, tr(getterResult));
                } else {
                    method.invoke(builder, getterResult);
                }
            }
        }
        @SuppressWarnings("unchecked")
        T result = (T) builderClass.getMethod("build").invoke(builder);

        return result;
    }

    private static boolean shouldBeTranslated(Method getter) {
        return getter.getAnnotation(Translate.class) != null;
    }

    private static <T> Method findSuitableGetter(T object, Parameter parameter) throws ReflectiveOperationException {
        return object.getClass().getMethod(localisationHelper.getterNameFromParameter(parameter));
    }


    private static Optional<Constructor<?>> findTranslateCreator(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            for (Annotation annotation : constructor.getAnnotations()) {
                if (annotation.annotationType() == TranslatableCreator.class) {
                    return Optional.of(constructor);
                }
            }
        }

        return Optional.empty();
    }

    private static Optional<Class<?>> findTranslateBuilder(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes())
                    .filter(s -> s.getName().contains("Builder")).collect(Collectors.toList());

            if (parameterTypes.size() > 0) {
                final Annotation[] annotations = parameterTypes.get(0).getAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == TranslatableBuilder.class) {
                        return Optional.of(parameterTypes.get(0));
                    }
                }
            }
        }
        return Optional.empty();
    }

}
