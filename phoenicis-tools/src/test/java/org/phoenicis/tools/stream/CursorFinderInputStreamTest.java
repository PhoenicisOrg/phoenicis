package org.phoenicis.tools.stream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CursorFinderInputStreamTest {
    private CursorFinderInputStream cursorFinderInputStream;

    @Test
    public void testNormalCase() throws IOException {
        this.cursorFinderInputStream = new CursorFinderInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()),
                "CD".getBytes());

        assertEquals("CDEFG", IOUtils.toString(cursorFinderInputStream, "UTF-8"));
    }

    @Test
    public void testEmptyCursor() throws IOException {
        this.cursorFinderInputStream = new CursorFinderInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()),
                new byte[0]);

        assertEquals("ABCDEFG", IOUtils.toString(cursorFinderInputStream, "UTF-8"));
    }

    @Test
    public void testDirectStart() throws IOException {
        this.cursorFinderInputStream = new CursorFinderInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()),
                "ABCDEFG".getBytes());

        assertEquals("ABCDEFG", IOUtils.toString(cursorFinderInputStream, "UTF-8"));
    }

    @Test
    public void testCursorNotFound() throws IOException {
        this.cursorFinderInputStream = new CursorFinderInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()),
                "IJ".getBytes());

        assertEquals("", IOUtils.toString(cursorFinderInputStream, "UTF-8"));
    }
}