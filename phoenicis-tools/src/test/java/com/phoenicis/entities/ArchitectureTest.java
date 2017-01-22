package com.phoenicis.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArchitectureTest {
    @Test
    public void getNameForWinePackages_64bits() throws Exception {
        assertEquals("amd64", Architecture.AMD64.getNameForWinePackages());
    }

    @Test
    public void getNameForWinePackages_32bits() throws Exception {
        assertEquals("x86", Architecture.I386.getNameForWinePackages());
    }
}