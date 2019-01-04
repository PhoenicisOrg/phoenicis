package org.phoenicis.tools.lnk;

/**
 * The FileAttributesFlags structure defines bits that specify the file attributes of the link target, if the target is
 * a file system item. File attributes can be used if the link target is not available, or if accessing the target would
 * be inefficient. It is possible for the target items attributes to be out of sync with this value.
 */
class LnkFileAttributeFlagsParser extends AbstractLnkFlagsParser {
    private static final int FILE_ATTRIBUTES_OFFSET = 24;

    private final static byte FILE_ATTRIBUTE_DIRECTORY = (byte) 0x10;

    LnkFileAttributeFlagsParser(byte[] rawLnkContent) {
        super(new byte[] {
                rawLnkContent[FILE_ATTRIBUTES_OFFSET],
                rawLnkContent[FILE_ATTRIBUTES_OFFSET + 1],
                rawLnkContent[FILE_ATTRIBUTES_OFFSET + 2],
                rawLnkContent[FILE_ATTRIBUTES_OFFSET + 3]
        });
    }

    /**
     * Determines is the .lnk is a directory
     *
     * @return true if it is a directory
     */
    boolean hasDirMask() {
        return testMask(FILE_ATTRIBUTE_DIRECTORY);
    }
}
