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

package org.phoenicis.tests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DTOContractTest {
    private final static int DEFAULT_MAX_DEPTH = 5;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private DTOContainer<?> DTOContainer;

    public DTOContractTest(DTOContainer<?> DTOContainer) {
        this.DTOContainer = DTOContainer;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<DTOContainer<?>> data() throws ClassNotFoundException {
        return findAllDTOs();
    }

    private static Optional<Constructor<?>> getConstructorAnnotatedWithJsonCreator(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            for (Annotation annotation : constructor.getAnnotations()) {
                if (annotation.annotationType() == JsonCreator.class) {
                    return Optional.of(constructor);
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<Constructor<?>> getConstructorHavingJsonBuilder(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes())
                    .filter(s -> s.getName().contains("Builder")).collect(Collectors.toList());

            if (parameterTypes.size() > 0) {
                final Annotation[] annotations = parameterTypes.get(0).getAnnotations();
                if (annotations.length > 0 && JsonPOJOBuilder.class.equals(annotations[0].annotationType())) {
                    return Optional.of(constructor);
                }
            }
        }
        return Optional.empty();
    }

    private static List<DTOContainer<?>> findAllDTOs() throws ClassNotFoundException {
        final List<DTOContainer<?>> fondJsonCreatorClasses = new ArrayList<>();

        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                true);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        final Stream<String> classNamesStream = scanner.findCandidateComponents("org.phoenicis").stream()
                .map(BeanDefinition::getBeanClassName);
        final Iterable<String> classNames = classNamesStream::iterator;

        for (String className : classNames) {
            final Class<?> clazz = Class.forName(className);
            if (isJsonClass(clazz)) {
                fondJsonCreatorClasses.add(new DTOContainer<>(clazz));
            }
        }

        return fondJsonCreatorClasses;
    }

    private static boolean isJsonClass(Class<?> clazz) {
        return getConstructorAnnotatedWithJsonCreator(clazz).isPresent()
                || getConstructorHavingJsonBuilder(clazz).isPresent();
    }

    @Test
    public void testConstructor() throws ReflectiveOperationException, URISyntaxException {
        final Object dto = newDTOInstance(DTOContainer.getClazz());
        assertNotNull(dto);
    }

    @Test
    public void testHashCodes() throws ReflectiveOperationException, URISyntaxException {
        final Object dto1 = newDTOInstance(DTOContainer.getClazz());
        final Object dto2 = newDTOInstance(DTOContainer.getClazz());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    public void testEqualsTrivialCases() throws ReflectiveOperationException, URISyntaxException {
        final Object dto = newDTOInstance(DTOContainer.getClazz());
        assertTrue(dto.equals(dto));
        assertFalse(dto.equals(null));
        assertFalse(dto.equals(new Object()));
    }

    @Test
    public void testSerialize() throws ReflectiveOperationException, JsonProcessingException, URISyntaxException {
        final Object dto = newDTOInstance(DTOContainer.getClazz());
        final String json = objectMapper.writeValueAsString(dto);
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    public void testDeserialize() throws ReflectiveOperationException, IOException, URISyntaxException {
        final Object dto = newDTOInstance(DTOContainer.getClazz());
        final String json = objectMapper.writeValueAsString(dto);

        final ObjectMapper alternativeObjectMapper = new ObjectMapper();
        alternativeObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final Object serialized = alternativeObjectMapper.readValue(json, DTOContainer.getClazz());

        assertEquals(dto, serialized);
    }

    private Object newDTOInstance(Class<?> clazz) throws ReflectiveOperationException, URISyntaxException {
        final Optional<Constructor<?>> builderConstructor = getConstructorHavingJsonBuilder(clazz);
        final Optional<Constructor<?>> simpleConstructor = getConstructorAnnotatedWithJsonCreator(clazz);

        if (simpleConstructor.isPresent()) {
            return createInstanceFromSimpleConstructor(simpleConstructor.get());
        } else if (builderConstructor.isPresent()) {
            return createInstanceFromBuilderConstructor(builderConstructor.get());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Object createInstanceFromSimpleConstructor(Constructor<?> simpleConstructor)
            throws ReflectiveOperationException, URISyntaxException {
        final List<Object> parameterValues = new ArrayList<>();

        for (Class<?> constructorParameterType : simpleConstructor.getParameterTypes()) {
            parameterValues.add(createInstanceOfParameter(constructorParameterType, DEFAULT_MAX_DEPTH));
        }

        return simpleConstructor.newInstance(parameterValues.toArray());
    }

    private Object createInstanceFromBuilderConstructor(Constructor<?> builderConstructor)
            throws ReflectiveOperationException, URISyntaxException {
        final Class<?> builder = builderConstructor.getParameterTypes()[0];
        final Object builderInstance = builder.newInstance();

        for (Method method : builder.getMethods()) {
            if (method.getName().startsWith("with")) {
                final Object result = method.invoke(builderInstance,
                        createInstanceOfParameter(method.getParameterTypes()[0], DEFAULT_MAX_DEPTH));

                if (result != builderInstance) {
                    throw new IllegalStateException("*with methods should return the current instance");
                }
            }
        }
        return builder.getMethod("build").invoke(builderInstance);
    }

    private Object createInstanceOfParameter(Class<?> constructorParameterType, int maxDepth)
            throws ReflectiveOperationException, URISyntaxException {

        if (constructorParameterType == byte.class || constructorParameterType == Byte.class) {
            return 0x42;
        }

        if (constructorParameterType == boolean.class || constructorParameterType == Boolean.class) {
            return true;
        }

        if (constructorParameterType == char.class || constructorParameterType == Character.class) {
            return 'a';
        }

        if (constructorParameterType == double.class || constructorParameterType == Double.class) {
            return 42.d;
        }

        if (constructorParameterType == float.class || constructorParameterType == Float.class) {
            return 42.f;
        }

        if (constructorParameterType == int.class || constructorParameterType == Integer.class) {
            return 42;
        }

        if (constructorParameterType == long.class || constructorParameterType == Long.class) {
            return 42L;
        }

        if (constructorParameterType == short.class || constructorParameterType == Short.class) {
            return 42;
        }

        if (constructorParameterType == String.class) {
            return "String";
        }

        if (constructorParameterType == Set.class) {
            return Collections.emptySet();
        }

        if (constructorParameterType == List.class) {
            return Collections.emptyList();
        }

        if (constructorParameterType == URI.class) {
            return new URI("http://www.test.uri");
        }

        if (isJsonClass(constructorParameterType)) {
            if (maxDepth == 0) {
                return null;
            }
            return newDTOInstance(constructorParameterType);
        }

        return null;
    }

    /**
     * This aim of the class is to customize the content of
     * the name of the test suit
     *
     * @param <C> The class the
     */
    private static class DTOContainer<C> {
        private final Class<C> clazz;

        private DTOContainer(Class<C> clazz) {
            this.clazz = clazz;
        }

        Class<C> getClazz() {
            return clazz;
        }

        @Override
        public String toString() {
            return clazz.getSimpleName();
        }
    }
}
