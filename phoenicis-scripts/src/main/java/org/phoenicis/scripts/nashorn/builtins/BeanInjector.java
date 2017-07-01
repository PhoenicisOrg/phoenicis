package org.phoenicis.scripts.nashorn.builtins;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.scripts.nashorn.NashornEngine;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * Injects Bean() function into a Script Engine
 */
public class BeanInjector implements EngineInjector {
    private final ApplicationContext applicationContext;

    public BeanInjector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void injectInto(NashornEngine nashornEngine) {
        nashornEngine.put("Bean", (Function<String, Object>) this::fetchBean, this::throwException);
    }

    private Object fetchBean(String beanName) {
        final Object bean = applicationContext.getBean(beanName);
        final Class<?> beanClass = bean.getClass();

        for (Annotation annotation : beanClass.getAnnotations()) {
            if (annotation.annotationType() == Safe.class) {
                return bean;
            }
        }

        throw new IllegalAccessError(String.format("You are not allowed to instanciate %s (of class %s) from a script.",
                beanName, beanClass.getName()));
    }
}
