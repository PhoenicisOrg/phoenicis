package org.phoenicis.scripts.engine.builtins;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.scripts.engine.PhoenicisScriptEngine;
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
    public void injectInto(PhoenicisScriptEngine phoenicisScriptEngine) {
        phoenicisScriptEngine.put("Bean", (Function<String, Object>) this::fetchBean, this::throwException);
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
