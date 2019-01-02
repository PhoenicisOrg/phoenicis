package org.phoenicis.tools.lnk;

import org.apache.commons.io.IOUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.tools.files.FilesManipulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This file parses .lnk files
 * https://msdn.microsoft.com/en-us/library/dd871305.aspx
 */
@Safe
public class LnkParser extends FilesManipulator {
    private final static int LNK_HEADER_SIZE = 0x4C;

    private final static int FILE_LOCATION_INFO_FLAG_OFFSET_OFFSET = 0x08;
    private final static int BASENAME_OFFSET_OFFSET = 0x10;
    private final static int NETWORK_VOLUME_TABLE_OFFSET_OFFSET = 0x14;
    private final static int FINALNAME_OFFSET_OFFSET = 0x18;
    private final static int SHARE_NAME_OFFSET_OFFSET = 0x08;

    /**
     * Parses a .lnk shortuct
     *
     * @param file The path to the file
     * @return a {@link LnkFile}
     * @throws IOException if any error occurs
     */
    public LnkFile parse(File file) throws IOException {
        this.assertInDirectory(file);
        try (final InputStream inputStream = new FileInputStream(file)) {
            return parse(inputStream);
        }
    }

    /**
     * Parses a .lnk shortuct
     *
     * @param rawLnkShortcutByteArray The byte array content of the sortcut
     * @return a {@link LnkFile}
     */
    public LnkFile parse(byte[] rawLnkShortcutByteArray) {
        final byte dataFlags = rawLnkShortcutByteArray[20];

        final LnkFileAttribute fileAttributes = fetchFilesAttributes(rawLnkShortcutByteArray);
        final LnkData lnkData = fetchLnkData(rawLnkShortcutByteArray);

        final boolean isDirectory = fileAttributes.hasDirMask();
        final boolean hasArguments = lnkData.hasArguments();

        final int fileStart = fetchFileStart(rawLnkShortcutByteArray, lnkData);
        final byte[] rawLnkContentWithoutHeader =
                Arrays.copyOfRange(rawLnkShortcutByteArray, fileStart, rawLnkShortcutByteArray.length - 1);

        final boolean isLocal = this.isLnkLocal(rawLnkContentWithoutHeader);
        final String fileName = parseLnkContent(rawLnkContentWithoutHeader, isLocal);

        return new LnkFile(isDirectory, isLocal, fileName, hasArguments);
    }

    private LnkData fetchLnkData(byte[] rawLnkShortcutByteArray) {
        return new LnkData(rawLnkShortcutByteArray);
    }

    /**
     * Parses a .lnk shortuct
     *
     * @param shortcutInputStream The input stream from the sortcut
     * @return a {@link LnkFile}
     * @throws IOException if any error occurs
     */
    LnkFile parse(InputStream shortcutInputStream) throws IOException {
        return parse(IOUtils.toByteArray(shortcutInputStream));
    }

    /**
     * Tests if the shortcuts points to a local file
     *
     * @param rawLnkContent The content
     * @return true or false
     */
    private boolean isLnkLocal(byte[] rawLnkContent) {
        final int fileLocationInfoFlag = rawLnkContent[FILE_LOCATION_INFO_FLAG_OFFSET_OFFSET];
        return (fileLocationInfoFlag & 2) == 0;
    }

    /**
     * Parse the acutal content of the shortcut
     *
     * @param rawLnkShortcutByteArrayWithoutHeader Raw lnk file content
     * @param isLocal                              Search for local or network filename
     */
    private String parseLnkContent(byte[] rawLnkShortcutByteArrayWithoutHeader, boolean isLocal) {
        final int finalNameOffset = rawLnkShortcutByteArrayWithoutHeader[FINALNAME_OFFSET_OFFSET];

        final String finalName = BytesUtilities.getNullDelimitedString(rawLnkShortcutByteArrayWithoutHeader, finalNameOffset);

        if (isLocal) {
            final int basenameOffset = rawLnkShortcutByteArrayWithoutHeader[BASENAME_OFFSET_OFFSET];
            final String basename = BytesUtilities.getNullDelimitedString(rawLnkShortcutByteArrayWithoutHeader, basenameOffset);
            return basename + finalName;
        } else {
            final int networkVolumeTableOffset = rawLnkShortcutByteArrayWithoutHeader[NETWORK_VOLUME_TABLE_OFFSET_OFFSET];
            final int shareNameOffset = rawLnkShortcutByteArrayWithoutHeader[networkVolumeTableOffset + SHARE_NAME_OFFSET_OFFSET] + networkVolumeTableOffset;
            final String shareName = BytesUtilities.getNullDelimitedString(rawLnkShortcutByteArrayWithoutHeader, shareNameOffset);
            return shareName + "\\" + finalName;
        }
    }

    /**
     * Fetches the start of the .lnk file once the header is removed
     *
     * @param rawLnkContent The content of the .lnk
     * @param dataFlags     The dataflags that will be used to determine if there is a shell section
     * @return The offset where the sortcuts file starts
     */
    private int fetchFileStart(byte[] rawLnkContent, LnkData dataFlags) {
        return LNK_HEADER_SIZE + fetchShellLength(rawLnkContent, dataFlags);
    }

    /**
     * Fetches shell section length
     *
     * @param rawLnkContent raw .lnk content
     * @param dataFlags {@link LnkData to fetch shell}
     * @return the size of the section
     */
    private int fetchShellLength(byte[] rawLnkContent, LnkData dataFlags) {
        int shellLength = 0;
        if (dataFlags.hasShell()) {
            final int lengthMarkerSize = 2;
            shellLength = BytesUtilities.bytes2short(rawLnkContent, LNK_HEADER_SIZE) + lengthMarkerSize;
        }

        return shellLength;
    }

    /**
     * Fetches the file attributes
     *
     * @param rawLnkContent raw .lnk content
     * @return a {@link LnkFileAttribute} property
     */
    private LnkFileAttribute fetchFilesAttributes(byte[] rawLnkContent) {
        return new LnkFileAttribute(rawLnkContent);
    }
}