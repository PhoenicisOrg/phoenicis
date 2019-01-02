package org.phoenicis.tools.lnk;

public class LnkData {
    private static final int DATA_FLAG_OFFSET = 20;

    private final static byte MASK_SHELL = (byte) 0x01;
    private final static byte MASK_RELATIVE_PATH = (byte) 0x8;
    private final static byte MASK_HAS_WORKING_DIR = (byte) 0x10;
    private final static byte MASK_HAS_ARGUMENTS = (byte) 0x20;
    private final static byte MASK_HAS_EXP_ICON = (byte) 0x4000;

    private final byte[] rawBytes;

    public LnkData(byte[] rawLnkContent) {
        this.rawBytes = new byte[] {
                rawLnkContent[DATA_FLAG_OFFSET],
                rawLnkContent[DATA_FLAG_OFFSET + 1],
                rawLnkContent[DATA_FLAG_OFFSET + 2],
                rawLnkContent[DATA_FLAG_OFFSET + 3]
        };
    }

    /**
     * Determines if the .lnk has a working directory
     *
     * @return true if the .lnk has a working directory
     */
    public boolean hasWorkingDir() {
        return (rawBytes[0] & MASK_HAS_WORKING_DIR) > 1;
    }

    /**
     * Determines if the .lnk has arguments
     *
     * @return true if the .lnk has arguments
     */
    public boolean hasArguments() {
        return (rawBytes[0] & MASK_HAS_ARGUMENTS) > 1;
    }

    public boolean hasShell() {
        return (rawBytes[0] & MASK_SHELL) > 0;
    }
}
