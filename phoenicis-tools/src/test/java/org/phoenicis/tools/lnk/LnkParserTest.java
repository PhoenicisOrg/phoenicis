package org.phoenicis.tools.lnk;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LnkParserTest {
    private final org.phoenicis.lnk.LnkParser delegated = mock(org.phoenicis.lnk.LnkParser.class);
    private final LnkParser lnkParser = new LnkParser(delegated);

    @Test
    public void testLnkParser_testPath() throws IOException {
        File fileData = mock(File.class);
        lnkParser.parse(fileData);
        verify(delegated).parse(fileData);
    }

}
