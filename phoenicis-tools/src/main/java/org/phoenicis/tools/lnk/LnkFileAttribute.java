package org.phoenicis.tools.lnk;

/**
 * Lnk file attribute flag
 */
class LnkFileAttribute extends AbstractLnkFlags {
    private static final int FILE_ATTRIBUTES_OFFSET = 24;

    private final static byte FILE_ATTRIBUTE_DIRECTORY = (byte) 0x10;

    LnkFileAttribute(byte[] rawLnkContent) {
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
