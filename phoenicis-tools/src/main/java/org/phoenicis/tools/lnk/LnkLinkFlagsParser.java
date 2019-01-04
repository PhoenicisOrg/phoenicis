package org.phoenicis.tools.lnk;

/**
 * The LinkFlags structure defines bits that specify which shell link structures are present in the file format after
 * the ShellLinkHeader structure (section 2.1).
 *
 * https://msdn.microsoft.com/en-us/library/dd871305.aspx
 */
class LnkLinkFlagsParser extends AbstractLnkFlagsParser {
    private static final int DATA_FLAG_OFFSET = 20;

    private final static byte MASK_HAS_LINK_TARGET_ID_LIST = (byte) 0x01;
    private final static byte MASK_HAS_NAME = (byte) 0x04;
    private final static byte MASK_RELATIVE_PATH = (byte) 0x8;
    private final static byte MASK_HAS_WORKING_DIR = (byte) 0x10;
    private final static byte MASK_HAS_ARGUMENTS = (byte) 0x20;
    private final static byte MASK_ICON_LOCATION = (byte) 0x40;

    LnkLinkFlagsParser(byte[] rawLnkContent) {
        super(new byte[] {
                rawLnkContent[DATA_FLAG_OFFSET],
                rawLnkContent[DATA_FLAG_OFFSET + 1],
                rawLnkContent[DATA_FLAG_OFFSET + 2],
                rawLnkContent[DATA_FLAG_OFFSET + 3]
        });
    }

    /**
     * Determines if the .lnk has a working directory
     *
     * @return true if the .lnk has a working directory
     */
    boolean hasWorkingDir() {
        return testMask(MASK_HAS_WORKING_DIR);
    }

    /**
     * Determines if the .lnk has arguments
     *
     * @return true if the .lnk has arguments
     */
    boolean hasArguments() {
        return testMask(MASK_HAS_ARGUMENTS);
    }

    boolean hasLinkTargetIdList() {
        return testMask(MASK_HAS_LINK_TARGET_ID_LIST);
    }

    boolean hasName() {
        return testMask(MASK_HAS_NAME);
    }

    boolean hasRelativePath() {
        return testMask(MASK_RELATIVE_PATH);
    }

    boolean hasIconLocation() {
        return testMask(MASK_ICON_LOCATION);
    }

    int fetchNumberOfStringData() {
        int number = 0;

        if (hasName()) {
            number++;
        }

        if (hasRelativePath()) {
            number++;
        }

        if (hasWorkingDir()) {
            number++;
        }

        if (hasArguments()) {
            number++;
        }

        if (hasIconLocation()) {
            number++;
        }

        return number;
    }
}
