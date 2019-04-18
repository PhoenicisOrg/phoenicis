package org.phoenicis.tools.lnk;

import org.apache.commons.io.IOUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.lnk.LnkFile;
import org.phoenicis.tools.files.FilesManipulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This file parses .lnk files
 * LNK is a file extension for a shortcut file used by Microsoft Windows to point to an executable file.
 *
 * https://msdn.microsoft.com/en-us/library/dd871305.aspx
 */
@Safe
public class LnkParser extends FilesManipulator {
    private final org.phoenicis.lnk.LnkParser delegated;

    public LnkParser(org.phoenicis.lnk.LnkParser delegated) {
        this.delegated = delegated;
    }

    /**
     * Parses a .lnk shortuct
     *
     * @param file The path to the file
     * @return a {@link LnkFile}
     * @throws IOException if any error occurs
     */
    public LnkFile parse(File file) throws IOException {
        return delegated.parse(file);
    }

    /**
     * Parses a .lnk shortuct
     *
     * @param rawLnkShortcutByteArray The byte array content of the sortcut
     * @return a {@link LnkFile}
     */
    public LnkFile parse(byte[] rawLnkShortcutByteArray) {
        return delegated.parse(rawLnkShortcutByteArray);
    }
}
