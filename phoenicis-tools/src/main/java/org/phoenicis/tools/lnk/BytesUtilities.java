package org.phoenicis.tools.lnk;

public class BytesUtilities {
    /*
     * Convert two little endian bytes into a short note
     */
    static int bytes2short(byte[] bytes, int offset) {
        return ((bytes[offset + 1] & 0xff) << 8) | (bytes[offset] & 0xff);
    }

    /**
     * Fetches a string inside a byte array delimited by an offset and the null characte r
     *
     * @param bytes The byte array
     * @param offset The start offset
     * @return The given string
     */
    static String getNullDelimitedString(byte[] bytes, int offset) {
        int len = 0;

        while (bytes[offset + len] != 0) {
            len++;
        }
        return new String(bytes, offset, len);
    }
}
