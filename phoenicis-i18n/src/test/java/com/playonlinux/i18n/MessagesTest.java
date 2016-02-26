package com.playonlinux.i18n;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessagesTest {
    @Test
    public void getStringTest() {
        assertEquals("Default", Messages.getString("DEFAULT"));
    }
}
