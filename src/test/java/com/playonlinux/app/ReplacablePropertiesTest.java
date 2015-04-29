package com.playonlinux.app;

import com.playonlinux.utils.ReplacableProperties;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ReplacablePropertiesTest {

    private ReplacableProperties properties;
    private final String PROPERTIES_FILENAME = "com/playonlinux/app/playonlinuxpropertiestest.properties";

    @Before
    public void setUp() throws IOException {
        this.properties = new ReplacableProperties();
        this.properties.load(ReplacablePropertiesTest.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
    @Test
    public void testGetProperty_TestSimpleValue_CorrectValueIsReturned() {
        assertEquals("test", getProperty("property1"));
        assertEquals("test2", getProperty("property2"));
        assertEquals("test3", getProperty("property3"));
    }

    @Test
    public void testGetProperty_TestVariableComposedValue_CorrectValueIsReturned() {
        assertEquals("test3 test2", getProperty("property4"));
        assertEquals("test3 test2 test3", getProperty("property5"));
    }

    @Test
    public void testGetProperty_TestVMPropertiesComposedValue_CorrectValueIsReturned() {
        assertEquals('/', getProperty("home_directory").getBytes()[0]);
    }
}