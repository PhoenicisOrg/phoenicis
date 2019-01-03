package org.phoenicis.tools.lnk;

class BytesUtilities {
    /**
     * Convert little endian bytes into an integer
     * @param bytes The byte
     * @param offset The offset to starts with
     * @param numberOfBytes The number of bytes
     *
     * @return An integer
     */
    static int bytes2int(byte[] bytes, int offset, int numberOfBytes) {
        int result = 0;

        for (int i = 0; i < numberOfBytes; i++) {
            result |= (bytes[offset + i] & 0xff) << i * 8;
        }

        return result;
    }

    /**
     * Fetches a string inside a byte array delimited by an offset and the null character
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
