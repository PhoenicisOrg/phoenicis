package org.phoenicis.tools.lnk;

import org.apache.commons.io.IOUtils;
import org.phoenicis.configuration.security.Safe;
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
        final LnkFileAttributeFlagsParser fileAttributes = fetchFilesAttributes(rawLnkShortcutByteArray);
        final LnkLinkFlagsParser lnkDataFlag = fetchLnkData(rawLnkShortcutByteArray);

        final boolean isDirectory = fileAttributes.hasDirMask();
        final boolean hasArguments = lnkDataFlag.hasArguments();

        final int fileStart = fetchFileStart(rawLnkShortcutByteArray, lnkDataFlag);
        final byte[] rawLnkContentWithoutHeader = Arrays.copyOfRange(rawLnkShortcutByteArray, fileStart,
                rawLnkShortcutByteArray.length - 1);

        final boolean isLocal = this.isLnkLocal(rawLnkContentWithoutHeader);
        final String fileName = parseLnkContent(rawLnkContentWithoutHeader, isLocal);

        final LnkStringData lnkStringData = fetchStringData(
                rawLnkContentWithoutHeader,
                lnkDataFlag);

        return new LnkFile(isDirectory, isLocal, fileName, hasArguments, lnkStringData);
    }

    /**
     * Fetches string data inside an array
     *
     * @param rawLnkContentWithoutHeader The byte array content of the sortcut
     * @param lnkDataFlags The lig data flags
     * @return a {@link LnkLinkFlagsParser}
     */
    private LnkStringData fetchStringData(byte[] rawLnkContentWithoutHeader,
            LnkLinkFlagsParser lnkDataFlags) {
        final int numberOfStrings = lnkDataFlags.fetchNumberOfStringData();
        final List<String> stringDatas = fetchStringData(rawLnkContentWithoutHeader, numberOfStrings);

        int nameIndex = 0;
        int relativePathIndex = 1;
        int workingDirIndex = 2;
        int commandLineArgumentsIndex = 3;
        int iconLocationIndex = 4;

        final Optional<String> name;
        final Optional<String> relativePath;
        final Optional<String> workingDir;
        final Optional<String> arguments;
        final Optional<String> iconLocation;

        if (lnkDataFlags.hasName()) {
            name = Optional.of(stringDatas.get(nameIndex));
        } else {
            name = Optional.empty();
            relativePathIndex--;
            workingDirIndex--;
            commandLineArgumentsIndex--;
            iconLocationIndex--;
        }

        if (lnkDataFlags.hasRelativePath()) {
            relativePath = Optional.of(stringDatas.get(relativePathIndex));
        } else {
            relativePath = Optional.empty();
            workingDirIndex--;
            commandLineArgumentsIndex--;
            iconLocationIndex--;
        }

        if (lnkDataFlags.hasWorkingDir()) {
            workingDir = Optional.of(stringDatas.get(workingDirIndex));
        } else {
            workingDir = Optional.empty();
            commandLineArgumentsIndex--;
            iconLocationIndex--;
        }

        if (lnkDataFlags.hasArguments()) {
            arguments = Optional.of(stringDatas.get(commandLineArgumentsIndex));
        } else {
            arguments = Optional.empty();
            iconLocationIndex--;
        }

        if (lnkDataFlags.hasArguments()) {
            iconLocation = Optional.of(stringDatas.get(iconLocationIndex));
        } else {
            iconLocation = Optional.empty();
        }

        return new LnkStringData(name, relativePath, workingDir, arguments, iconLocation);
    }

    /**
     * Fetches string data inside an array
     *
     * @param rawLnkContentWithoutHeader Raw lnk file content
     * @param numberOfStringToRead The number of string datas to read
     * @return a list of string containing string datas
     */
    private List<String> fetchStringData(byte[] rawLnkContentWithoutHeader,
            int numberOfStringToRead) {
        final List<String> stringDatas = new ArrayList<>();

        final int linkInfoSize = BytesUtilities.bytes2int(rawLnkContentWithoutHeader, 0, 3);

        int index = linkInfoSize;
        for (int i = 0; i < numberOfStringToRead; i++) {
            final int stringSize = BytesUtilities.bytes2int(rawLnkContentWithoutHeader, index, 2);

            final String decodedString = new String(Arrays.copyOfRange(
                    rawLnkContentWithoutHeader, index + 2, index + (stringSize) * 2), Charset.forName("UTF-16LE"));
            stringDatas.add(decodedString);

            index = index + (stringSize) * 2 + 2;
        }

        return stringDatas;
    }

    /**
     * Fetches LnkLinkFlagsParser
     *
     * @param rawLnkShortcutByteArray Raw lnk file content
     * @return The Data Flags
     * @see LnkLinkFlagsParser
     */
    private LnkLinkFlagsParser fetchLnkData(byte[] rawLnkShortcutByteArray) {
        return new LnkLinkFlagsParser(rawLnkShortcutByteArray);
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
     * Parse the actual content of the shortcut
     *
     * @param rawLnkShortcutByteArrayWithoutHeader Raw lnk file content
     * @param isLocal Search for local or network filename
     */
    private String parseLnkContent(byte[] rawLnkShortcutByteArrayWithoutHeader, boolean isLocal) {
        final int finalNameOffset = rawLnkShortcutByteArrayWithoutHeader[FINALNAME_OFFSET_OFFSET];

        final String finalName = BytesUtilities.getNullDelimitedString(rawLnkShortcutByteArrayWithoutHeader,
                finalNameOffset);

        if (isLocal) {
            final int basenameOffset = rawLnkShortcutByteArrayWithoutHeader[BASENAME_OFFSET_OFFSET];
            final String basename = BytesUtilities.getNullDelimitedString(rawLnkShortcutByteArrayWithoutHeader,
                    basenameOffset);
            return basename + finalName;
        } else {
            final int networkVolumeTableOffset = rawLnkShortcutByteArrayWithoutHeader[NETWORK_VOLUME_TABLE_OFFSET_OFFSET];
            final int shareNameOffset = rawLnkShortcutByteArrayWithoutHeader[networkVolumeTableOffset
                    + SHARE_NAME_OFFSET_OFFSET] + networkVolumeTableOffset;
            final String shareName = BytesUtilities.getNullDelimitedString(rawLnkShortcutByteArrayWithoutHeader,
                    shareNameOffset);
            return shareName + "\\" + finalName;
        }
    }

    /**
     * Fetches the start of the .lnk file once the header is removed
     *
     * @param rawLnkContent The content of the .lnk
     * @param dataFlags The dataflags that will be used to determine if there is a shell section
     * @return The offset where the sortcuts file starts
     */
    private int fetchFileStart(byte[] rawLnkContent, LnkLinkFlagsParser dataFlags) {
        return LNK_HEADER_SIZE + fetchLinkTargetIdListLength(rawLnkContent, dataFlags);
    }

    /**
     * Fetches shell section length
     *
     * @param rawLnkContent raw .lnk content
     * @param dataFlags {@link LnkLinkFlagsParser to fetch shell}
     * @return the size of the section
     */
    private int fetchLinkTargetIdListLength(byte[] rawLnkContent, LnkLinkFlagsParser dataFlags) {
        int linkTargetIdListLength = 0;
        if (dataFlags.hasLinkTargetIdList()) {
            final int lengthMarkerSize = 2;
            linkTargetIdListLength = BytesUtilities.bytes2int(rawLnkContent, LNK_HEADER_SIZE, 2) + lengthMarkerSize;
        }

        return linkTargetIdListLength;
    }

    /**
     * Fetches the file attributes
     *
     * @param rawLnkContent raw .lnk content
     * @return a {@link LnkFileAttributeFlagsParser} property
     */
    private LnkFileAttributeFlagsParser fetchFilesAttributes(byte[] rawLnkContent) {
        return new LnkFileAttributeFlagsParser(rawLnkContent);
    }
}
