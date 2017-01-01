package com.playonlinux.apps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NullApplicationSourceTest {
    @Test
    public void testNullApplicationSource() {
        assertEquals(0, new NullApplicationSource().fetchInstallableApplications().size());
    }
}