package com.playonlinux.i18n;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessagesTest {
    @Test
    public void getStringTest() {
        assertEquals("test", Messages.getString("test"));
    }
}
