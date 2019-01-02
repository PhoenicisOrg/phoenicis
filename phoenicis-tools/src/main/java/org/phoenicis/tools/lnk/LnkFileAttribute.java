package org.phoenicis.tools.lnk;

public class LnkFileAttribute {
    private static final int FILE_ATTRIBUTES_OFFSET = 24;

    private final static byte FILE_ATTRIBUTE_DIRECTORY = (byte) 0x10;

    private final byte[] rawBytes;

    public LnkFileAttribute(byte[] rawLnkContent) {
        this.rawBytes = new byte[] {
                rawLnkContent[FILE_ATTRIBUTES_OFFSET],
                rawLnkContent[FILE_ATTRIBUTES_OFFSET + 1],
                rawLnkContent[FILE_ATTRIBUTES_OFFSET + 2],
                rawLnkContent[FILE_ATTRIBUTES_OFFSET + 3]
        };
    }

    public boolean hasDirMask() {
        return (rawBytes[0] & FILE_ATTRIBUTE_DIRECTORY) > 1;
    }
}
