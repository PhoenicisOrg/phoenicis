package com.playonlinux.ui.dtos;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ShortcutDTOTest {

    private ShortcutDTO shortcutDto;

    @Before
    public void setUp() {
        this.shortcutDto = new ShortcutDTO.Builder()
                .withName("Name")
                .withIcon(new File("/tmp/icon"))
                .build();
    }
    @Test
    public void testShortcutDTO_CreateDTO_nameIsPopulated() throws Exception {
        assertEquals("Name", shortcutDto.getName());
    }

    @Test
    public void testShortcutDTO_CreateDTO_iconIsPopulated() throws Exception {
        assertEquals("/tmp/icon", shortcutDto.getIcon().getAbsolutePath());
    }
}