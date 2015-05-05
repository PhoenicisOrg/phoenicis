package com.playonlinux.injection;

import java.util.HashMap;


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
        HashMap<Class<?>, Object> beans = injector.loadAllBeans(this);
        injector.injectAllBeans(strictLoadingPolicy, beans);
    }


}
