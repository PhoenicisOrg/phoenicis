package org.phoenicis.tools.lnk;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class LnkParserTest {
    private final InputStream game1inputStream = getClass().getResourceAsStream("xiii.lnk");
    private final InputStream game2inputStream = getClass().getResourceAsStream("teenagent.lnk");
    private LnkFile game1;
    private LnkFile game2;

    @Before
    public void setUp() throws IOException {
        this.game1 = new LnkParser().parse(game1inputStream);
        this.game2 = new LnkParser().parse(game2inputStream);
    }

    @Test
    public void testLnkParser_testPath() {
        assertEquals("C:\\GOG Games\\XIII\\system\\xiii.exe", game1.getRealFilename());
    }

    @Test
    public void testLnkParser_testPath_dosbox() {
        assertEquals("c:\\gog games\\teenagent\\DOSBOX\\dosbox.exe", game2.getRealFilename());
    }

    @Test
    public void testLnkParser_testIsDirectory() {
        assertFalse(game1.isDirectory());
    }

    @Test
    public void testLnkParser_testHasArguments_noArguments() {
        assertTrue(game1.isHasArguments());
    }

    @Test
    public void testLnkParser_testHasArguments_arguments() {
        assertTrue(game2.isHasArguments());
    }
}