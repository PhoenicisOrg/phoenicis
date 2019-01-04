package org.phoenicis.tools.lnk;

/**
 * This class represent a byte flags.
 * It helps to apply a mask on the byte flags and allows reading them
 */
class AbstractLnkFlagsParser {
    protected final byte[] rawBytes;

    protected AbstractLnkFlagsParser(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    /**
     * Tests the flags with a mask (stored in a byte)
     * @param flags The flags
     * @return True if the flags matches the mask
     */
    protected boolean testMask(byte flags) {
        return (rawBytes[0] & flags) > 0;
    }
}
