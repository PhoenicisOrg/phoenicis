package com.playonlinux.injection;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

@Component
public class InjectorTest extends AbstractConfigFile {

    @Inject
    static String checkInjectedString;

    @Inject
    static File unmappedDependecy;

    @Bean
    String injectedString() {
        return "Injection is working!";
    }

    @Test
    public void testInjector_InjectAString_StringIsInjected() throws InjectionException {
        this.setStrictLoadingPolicy(false);
        this.load();
        assertEquals(this.injectedString(), checkInjectedString);
    }

    @Test(expected=InjectionException.class)
    public void testInjector_InjectAStringWithStrictPolicyAndUnmappedDependency_ThrowsException()
            throws InjectionException {
        this.setStrictLoadingPolicy(true);
        this.load();
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }
}