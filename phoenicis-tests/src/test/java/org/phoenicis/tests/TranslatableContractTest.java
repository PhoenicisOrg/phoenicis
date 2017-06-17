package org.phoenicis.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.phoenicis.configuration.localisation.TranslatableBuilder;
import org.phoenicis.configuration.localisation.TranslatableCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.phoenicis.configuration.localisation.Localisation.isTranslatable;

@RunWith(Parameterized.class)
public class TranslatableContractTest {
    private final static int DEFAULT_MAX_DEPTH = 5;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private TranslatableContainer<?> translatableContainer;

    public TranslatableContractTest(TranslatableContainer<?> translatableContainer) {
        this.translatableContainer = translatableContainer;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<TranslatableContainer<?>> data() throws ClassNotFoundException {
        return findAllTranslatables();
    }

    private static Optional<Constructor<?>> getTranslationConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            for (Annotation annotation : constructor.getAnnotations()) {
                if (annotation.annotationType() == TranslatableCreator.class) {
                    return Optional.of(constructor);
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<Constructor<?>> getTranslationBuilder(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes())
                    .filter(s -> s.getName().contains("Builder")).collect(Collectors.toList());

            if (parameterTypes.size() > 0) {
                final Annotation[] annotations = parameterTypes.get(0).getAnnotations();

                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == TranslatableBuilder.class) {
                        return Optional.of(constructor);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static List<TranslatableContainer<?>> findAllTranslatables() throws ClassNotFoundException {
        final List<TranslatableContainer<?>> foundClasses = new ArrayList<>();

        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                true);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        final Stream<String> classNamesStream = scanner.findCandidateComponents("org.phoenicis").stream()
                .map(BeanDefinition::getBeanClassName);
        final Iterable<String> classNames = classNamesStream::iterator;

        for (String className : classNames) {
            final Class<?> clazz = Class.forName(className);
            if (isTranslatable(clazz)) {
                foundClasses.add(new TranslatableContainer<>(clazz));
            }
        }

        return foundClasses;
    }

    @Test
    public void testConstructor() throws ReflectiveOperationException, URISyntaxException {
        final Object translatable = newTranslatableInstance(translatableContainer.getClazz());
        assertNotNull(translatable);
    }

    @Test
    public void testHashCodes() throws ReflectiveOperationException, URISyntaxException {
        final Object translatable1 = newTranslatableInstance(translatableContainer.getClazz());
        final Object translatable2 = newTranslatableInstance(translatableContainer.getClazz());
        assertEquals(translatable1, translatable2);
        assertEquals(translatable1.hashCode(), translatable2.hashCode());
    }

    @Test
    public void testEqualsTrivialCases() throws ReflectiveOperationException, URISyntaxException {
        final Object translatable = newTranslatableInstance(translatableContainer.getClazz());
        assertTrue(translatable.equals(translatable));
        assertFalse(translatable.equals(null));
        assertFalse(translatable.equals(new Object()));
    }

    private Object newTranslatableInstance(Class<?> clazz) throws ReflectiveOperationException, URISyntaxException {
        final Optional<Constructor<?>> builderConstructor = getTranslationBuilder(clazz);
        final Optional<Constructor<?>> simpleConstructor = getTranslationConstructor(clazz);

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

        if (constructorParameterType == Map.class) {
            return Collections.emptyMap();
        }

        if (constructorParameterType == URI.class) {
            return new URI("http://www.test.uri");
        }

        if (isTranslatable(constructorParameterType)) {
            if (maxDepth == 0) {
                return null;
            }
            return newTranslatableInstance(constructorParameterType);
        }

        return null;
    }

    /**
     * This aim of the class is to customize the content of
     * the name of the test suit
     *
     * @param <C> The class the
     */
    private static class TranslatableContainer<C> {
        private final Class<C> clazz;

        private TranslatableContainer(Class<C> clazz) {
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