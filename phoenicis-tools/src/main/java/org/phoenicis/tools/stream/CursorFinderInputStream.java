package org.phoenicis.tools.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * This input stream will takes an input stream, a cursor (a byte-array) and find it
 * before creating a sub input stream that skip all data before the cursor.
 * Example:
 * <p>
 * Let's suppose we have an input stream that is sending 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06...
 * Then, if the cursor is [0x02, 0x03], the new input stream will send 0x02, 0x03, 0x04, 0x05, 0x06...
 */
public class CursorFinderInputStream extends InputStream {
    private final byte[] cursor;
    private final InputStream inputStream;
    private int cursorPosition = 0;
    private int readPosition = 0; // Once the cursor is found, we need to send it through the input stream.

    /**
     * @param inputStream The input stream source
     * @param cursor The cursor to find
     */
    public CursorFinderInputStream(InputStream inputStream, byte[] cursor) {
        this.inputStream = inputStream;
        this.cursor = cursor;
    }

    @Override
    public int read() throws IOException {
        while (true) {
            if (cursorHasBeenFound()) {
                if (!cursorHasBeenRead()) {
                    return cursor[readPosition++];
                }
                return inputStream.read();
            }

            int delegatedInputStreamValue = inputStream.read();

            if (delegatedInputStreamValue == -1) {
                return -1;
            }

            if (cursor[cursorPosition] == delegatedInputStreamValue) {
                cursorPosition++;
            } else {
                cursorPosition = 0;
            }
        }
    }

    private boolean cursorHasBeenRead() {
        return readPosition >= cursor.length;
    }

    private boolean cursorHasBeenFound() {
        return cursorPosition >= cursor.length;
    }
}
