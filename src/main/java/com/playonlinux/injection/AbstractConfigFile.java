package com.playonlinux.injection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class AbstractConfigFile {
    protected abstract String definePackage();

    /**
     * If this is set to true, the injector will throw an exception if a non defined bean in the config file
     * is being injected. Otherwise, the object will stay to null (useful for a testing context)
     */
    boolean strictLoadingPolicy = true;

    public void setStrictLoadingPolicy(Boolean strictLoadingPolicy) {
        this.strictLoadingPolicy = strictLoadingPolicy;
    }
    public void load() throws InjectionException {
        Injector injector = new Injector(definePackage());
        HashMap<Class<?>, Object> beans = loadAllBeans(injector);
        injectAllBeans(injector, beans);
    }

    protected void injectAllBeans(Injector injector, HashMap<Class<?>, Object> beans) throws InjectionException {
        Set<Class<?>> componentClasses = injector.getComponentClasses();

        for(Class<?> componentClass: componentClasses) {
            List<Field> fields = injector.getAnnotatedFields(componentClass, Inject.class);
            for(Field field: fields){
                if(strictLoadingPolicy && !beans.containsKey(field.getType())) {
                    throw new InjectionException(String.format("Unable to inject %s. Check your config file",
                            field.getType().toString()));
                } else if(beans.containsKey(field.getType())){
                    try {
                        field.setAccessible(true);
                        field.set(null, beans.get(field.getType()));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw new InjectionException(String.format("Unable to inject %s. Error while injecting: %s",
                                field.getType().toString(), e));
                    }
                }
            }
        }
    }

    private HashMap<Class<?>, Object> loadAllBeans(Injector injector) throws InjectionException {
        List<Method> methods = injector.getAnnotatedMethods(this.getClass(), Bean.class);

        HashMap<Class<?>, Object> beans = new HashMap<>();

        for(Method method: methods) {
            method.setAccessible(true);
            try {
                beans.put(method.getReturnType(), method.invoke(this));
            } catch (IllegalAccessException e) {
                throw new InjectionException(String.format("Unable to inject dependencies (IllegalAccessException): %s", e));
            } catch (InvocationTargetException e) {
                throw new InjectionException(String.format("Unable to inject dependencies (InvocationTargetException): %s", e));
            }
        }
        return beans;
    }
}
