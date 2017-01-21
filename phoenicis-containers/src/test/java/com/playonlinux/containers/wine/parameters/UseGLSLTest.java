package com.playonlinux.containers.wine.parameters;

import com.playonlinux.win32.registry.RegistryWriter;
import org.junit.Test;

public class UseGLSLTest extends AbstractRegistryParameterTest<UseGLSL> {
    private final RegistryWriter registryWriter = new RegistryWriter();

    @Test
    public void testGenerateRegFile_enabledGLSL() {
        testStringValue(UseGLSL.ENABLED, "enabled");
    }

    @Test
    public void testGenerateRegFile_disabledGLSL() {
        testStringValue(UseGLSL.DISABLED, "disabled");
    }

    @Test
    public void testGenerateRegFile_defaultGLSL() {
        testRemoveValue(UseGLSL.DEFAULT);
    }

}